package com.smart.contact.user.controller;

import com.smart.contact.contacts.model.Contact;
import com.smart.contact.contacts.service.ContactService;
import com.smart.contact.user.model.User;
import com.smart.contact.user.service.UserService;
import com.smart.contact.util.DateDto;
import com.smart.contact.util.HTMLExporter;
import com.smart.contact.util.Message;
import com.smart.contact.util.PasswordDto;
import com.smart.contact.util.PasswordMatcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute
    private void common(Model model, Principal principal, HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null && principal != null) {
            System.out.println("Fetched User.....");
            user = userService.findByUsername(principal.getName()).orElse(new User());
            session.setAttribute("user", (User) user);
        }
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Smart Contact Manager | Home");
        return "home";
    }

    @GetMapping("/user/dashboard")
    public String dashboardGet(Model model, HttpSession session) {
        User user = dashboard(model, session);
        model.addAttribute("data", chartData(user, ""+LocalDate.now().getYear()));
        model.addAttribute("currentYear",LocalDate.now().getYear());
        return "user/dashboard";
    }

    @PostMapping("/user/dashboard")
    public String dashboardPost(@RequestParam("year") String year, Model model, HttpSession session) {
        User user = dashboard(model, session);
        model.addAttribute("data", chartData(user, year));
        model.addAttribute("currentYear",year);
        return "user/dashboard";
    }

    private User dashboard(Model model, HttpSession session) {
        model.addAttribute("title", "Contact Dashboard");

        User user = (User) session.getAttribute("user");

        model.addAttribute("user", user);
        model.addAttribute("userDate", DateDto.getDateLong(LocalDate.parse(user.getDate(), DateDto.FORMAT)));

        model.addAttribute("totalCount", contactService.countByUser(user));
        model.addAttribute("todayCount", contactService.countByUserAndToday(user));
        model.addAttribute("weekCount", contactService.countByUserAndWeek(user, DateDto.getWeek(LocalDate.now().format(DateDto.FORMAT), DateDto.FORMAT)));
        model.addAttribute("monthCount", contactService.countByUserAndWeek(user, DateDto.getMonth(LocalDate.now().format(DateDto.FORMAT), DateDto.FORMAT)));

        model.addAttribute("years",years(user));
        return user;
    }

    @GetMapping(path = {"/user/profile", "/user/edit-profile"})
    public String profile(Model model, HttpSession session, HttpServletRequest request) {
        model.addAttribute("title", "User Profile");

        User user = (User) session.getAttribute("user");

        model.addAttribute("user", user);
        model.addAttribute("createDate", DateDto.getDateLong(LocalDate.parse(user.getDate(), DateDto.FORMAT)));
        model.addAttribute("profilePic", "data:image/png;base64," + Base64.getEncoder().encodeToString(user.getImg()));

        if (request.getRequestURL().toString().endsWith("edit-profile")) {
            model.addAttribute("title", "Edit User Details");
            return "user/editProfile";
        }
        return "user/profile";
    }

    @PostMapping("/user/edit-profile")
    public String profileEdit(@Valid @ModelAttribute("user") User user, BindingResult result,
                              @RequestParam("profimg") MultipartFile img,
                              Model model, HttpSession session) {
        user = fillData(user, session);

        model.addAttribute("title", "Save User Profile");
        model.addAttribute("createDate", DateDto.getDateLong(LocalDate.parse(user.getDate(), DateDto.FORMAT)));
        model.addAttribute("profilePic", "data:image/png;base64," + Base64.getEncoder().encodeToString(user.getImg()));

        List<FieldError> collect = result.getFieldErrors().stream().filter(f -> !f.getField().equals("username"))
                .filter(f -> !f.getField().equals("password"))
                .collect(Collectors.toList());

        if (collect.size() > 0) {
            System.out.println(collect);
            return "user/editProfile";
        }

        try {
            if (!img.isEmpty()) {
                user.setImg(img.getBytes());
            }
            User save = userService.save(user);
            System.out.println(save);
            if (save.getId() > 0) {
                session.setAttribute("user", save);
                session.setAttribute("message", new Message("alert-success", "User Account Data Updated Successfully"));

                return "redirect:/user/profile";
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("alert-danger", "Error Occurred: " + e.getMessage()));
        }

        return "user/editProfile";
    }

    private User fillData(User user, HttpSession session) {
        User orgUser = (User) session.getAttribute("user");
        user.setId(orgUser.getId());
        user.setUsername(orgUser.getUsername());
        user.setPassword(orgUser.getPassword());
        user.setEnabled(true);
        user.setRole(orgUser.getRole());
        user.setContactList(orgUser.getContactList());
        user.setDate(orgUser.getDate());
        user.setImg(orgUser.getImg());
        return user;
    }


    @PostMapping("/user/authenticate")
    public String authenticate(@RequestParam("username") String username, @RequestParam("password") String password,
                               Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user.getUsername().equals(username) && passwordEncoder.matches(password, user.getPassword())) {
            session.setAttribute("auth", true);
            model.addAttribute("title", "Change Password");
            return "redirect:/user/change-password";
        } else {
            model.addAttribute("title", "Contact Dashboard");
            session.setAttribute("message", new Message("bg-danger", "Bad Credentials"));
            return "redirect:/user/dashboard";
        }
    }

    @GetMapping("/user/change-password")
    public String changePassword(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        Object auth = session.getAttribute("auth");
        if (auth == null) {
            model.addAttribute("title", "Contact Dashboard");
            session.setAttribute("message", new Message("bg-danger", "Nice Try, But It Won't Work"));
            return "redirect:/user/dashboard";
        } else {
            model.addAttribute("title", "Change Password");
            model.addAttribute("passwordDto", new PasswordDto("", ""));
            session.removeAttribute("auth");
            return "user/changePassword";
        }
    }

    @PostMapping("/user/change-password")
    public String updatePassword(@ModelAttribute("passwordDto") PasswordDto passwordDto, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("title", "Change Password");
        model.addAttribute("user", user);
        if (passwordDto.getPassword().equals(passwordDto.getCpassword())) {
            if (passwordDto.isEmpty()) {
                session.setAttribute("message", new Message("bg-danger", "Passwords Cannot Be Empty"));
            } else if (!PasswordMatcher.validate(passwordDto.getPassword())) {
                session.setAttribute("message", new Message("bg-danger", "Passwords must contain 8-20 characters including atleast one of each of Uppercase, Lowercase, Digits and Special Symbols [#, *, +, $, %, @, !]"));
            } else {
                user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
                System.out.println(user.getPassword());
                try {
                    User save = userService.save(user);
                    System.out.println(save.getPassword());
                    if (save.getPassword().equals(user.getPassword())) {
                        session.setAttribute("message", new Message("bg-success", "Passwords Changed Successfully"));
                        session.setAttribute("user", save);
                        model.addAttribute("user", save);
                    } else {
                        session.setAttribute("message", new Message("bg-danger", "Passwords Updation Failed"));
                    }
                } catch (Exception e) {
                    session.setAttribute("message", new Message("bg-danger", "Exception: " + e.getMessage()));
                }
            }
        } else {
            session.setAttribute("message", new Message("bg-danger", "Passwords Doesn't Match"));
        }
        return "user/changePassword";
    }

    private Map<String, Long> chartData(User user, String year) {
        Map<Integer, Long> collect = contactService.findByUser(user).parallelStream()
                .filter(f->f.getDate().startsWith(year))
                .collect(Collectors.groupingBy(m -> parse(m.getDate()).getMonthValue(), Collectors.counting()));

        TreeMap<String, Long> data = new TreeMap<>(Comparator.comparing(Month::valueOf));
        for (int i = 1; i <= 12; i++) {
            data.put(Month.of(i).toString(), collect.getOrDefault(i, 0L));
        }
        return data;
    }

    private LocalDate parse(String date) {
        return LocalDate.parse(date, DateDto.FORMAT);
    }

    private List<String> years(User user){
        return contactService.findByUser(user).parallelStream()
                .map(m -> m.getDate().substring(0, 4))
                .distinct()
                .collect(Collectors.toList());
    }
}
