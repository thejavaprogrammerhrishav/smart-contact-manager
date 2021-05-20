package com.smart.contact.user.controller;

import com.smart.contact.user.model.User;
import com.smart.contact.user.service.UserService;
import com.smart.contact.util.DateDto;
import com.smart.contact.util.Message;
import com.smart.contact.util.PasswordMatcher;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signup(Model model, HttpServletRequest request) {
        model.addAttribute("title", "User Registration");
        model.addAttribute("user", new User());
        if(request.isUserInRole("ROLE_NORMAL")){
            return "redirect:/loginSuccess";
        }
        return "signup";
    }

    @PostMapping("/doRegister")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result,
                           @RequestParam("cpassword") String cpassword,
                           @RequestParam("profimg") MultipartFile img,
                           HttpSession session, Model model) {
        model.addAttribute("title", "User Registration");
        if (user.getPassword().equals(cpassword)) {
            if (PasswordMatcher.validate(user.getPassword())) {
                if (result.hasErrors()) {
                    System.out.println(result);
                    return "signup";
                }
                try {
                    if (img.isEmpty()) {
                        throw new Exception("Please Select A Profile Image");
                    } else {
                        user.setImg(img.getBytes());
                    }

                    if (userService.findByUsername(user.getUsername()).isPresent()) {
                        throw new Exception("Username already taken");
                    }
                    user.setRole("ROLE_NORMAL");
                    user.setEnabled(true);
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setDate(DateDto.getDateNormal(LocalDate.now()));
                    User save = userService.save(user);
                    if (save.getId() > 0) {
                        model.addAttribute("user", new User());
                        session.setAttribute("message", new Message("alert-success", "User Account Created Successfully"));
                    }
                } catch (Exception e) {
                    session.setAttribute("message", new Message("alert-danger", "Error Occurred: " + e.getMessage()));
                }

            } else {
                session.setAttribute("message", new Message("alert-danger", "Passwords must contain 8-20 characters including atleast one of each of Uppercase, Lowercase, Digits and Special Symbols [#, *, +, $, %, @, !]"));
            }
        } else {
            session.setAttribute("message", new Message("alert-danger", "Passwords doesn't match"));
        }
        return "redirect:/signup";
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request){
        model.addAttribute("title","Login");
        if(request.isUserInRole("ROLE_NORMAL")){
            return "redirect:/loginSuccess";
        }
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String redirect(HttpServletRequest request){
        if(request.isUserInRole("ROLE_NORMAL")){
            return "redirect:/user/dashboard";
        }

        return "redirect:/login?error=Unauthorised Access Request";
    }
}
