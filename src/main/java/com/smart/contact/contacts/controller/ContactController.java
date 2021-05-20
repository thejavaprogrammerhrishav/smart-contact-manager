package com.smart.contact.contacts.controller;

import com.smart.contact.contacts.model.Contact;
import com.smart.contact.contacts.service.ContactService;
import com.smart.contact.user.model.User;
import com.smart.contact.user.service.UserService;
import com.smart.contact.util.DateDto;
import com.smart.contact.util.Message;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ContactController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @ModelAttribute
    private void common(Model model, Principal principal, HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            System.out.println("Fetched User.....");
            user = userService.findByUsername(principal.getName()).orElse(new User());
            session.setAttribute("user", (User) user);
        }
        User thisUser = (User) user;
        model.addAttribute("user", thisUser);
    }

    @GetMapping("/user/add-contact")
    public String addContact(Model model, HttpSession session) {
        model.addAttribute("title", "Add New Contact");
        model.addAttribute("contact", new Contact());
        return "user/addContact";
    }

    @GetMapping("/user/save-contact")
    public String redirectSave() {
        return "redirect:/user/add-contact";
    }

    @PostMapping("/user/save-contact")
    public String saveContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result, HttpSession session,
                              @RequestParam("profimg") MultipartFile img, Model model) {

        if (result.hasErrors()) {
            return "/user/addContact";
        }

        try {
            if (img.isEmpty()) {
                session.setAttribute("message", new Message("alert-danger", "Please Select An Image"));
            } else {
                contact.setImg(img.getBytes());
                contact.setDate(LocalDate.now().format(DateDto.FORMAT));

                User user = (User) session.getAttribute("user");

                contact.setUser(user);

                Contact res = contactService.save(contact);
                if (res.getId() > 0) {
                    session.setAttribute("message", new Message("alert-success", "Contact Saved Successfully"));
                } else {
                    session.setAttribute("message", new Message("alert-danger", "Contact Saving Failed"));
                }
            }
        } catch (Exception e) {
            session.setAttribute("message", new Message("alert-danger", "Error Occurred: " + e.getMessage()));
        }

        return "redirect:/user/add-contact";
    }

    @GetMapping(path = {"/user/all-contact", "/user/contact"})
    public String allContact(Model model, HttpSession session) {
        model.addAttribute("title", "All Contacts");
        model.addAttribute("searchBy", "select");
        model.addAttribute("search", "");

        User user = (User) session.getAttribute("user");

        List<Contact> contacts = user.getContactList();
        model.addAttribute("contacts", contacts);

        return "user/allContact";
    }

    @PostMapping("/user/all-contact")
    public String allContactSearch(@RequestParam("searchBy") String searchBy, @RequestParam("search") String search,
                                   Model model, HttpSession session) {

        model.addAttribute("searchBy", searchBy);
        model.addAttribute("search", search);

        User user = (User) session.getAttribute("user");

        model.addAttribute("title", "All Contacts");

        List<Contact> contacts = user.getContactList();

        if (searchBy.equals("select")) {
            session.setAttribute("message", new Message("alert-danger", "Please Select Search By Category"));
        } else if (search.isEmpty()) {
            session.setAttribute("message", new Message("alert-danger", "Please Enter Text To Search"));
        } else {
            if (searchBy.equals("name")) {
                contacts = contactService.findByUserAndName(user, search);
            } else if (searchBy.equals("nickname")) {
                contacts = contactService.findByUserAndNickname(user, search);
            } else if (searchBy.equals("work")) {
                contacts = contactService.findByUserAndWork(user, search);
            } else if (searchBy.equals("contact")) {
                contacts = contactService.findByUserAndContact(user, search);
            } else {
                // do nothing;
            }
        }
        model.addAttribute("contacts", contacts);
        return "user/allContact";
    }

    @GetMapping("/user/contact/{id}")
    public String viewContact(@PathVariable("id") Long id, Model model, HttpSession session) {
        model.addAttribute("title", "View Contact");

        if (id == null || id <= 0) {
            session.setAttribute("message", new Message("alert-danger", "Invalid Contact"));
            return "redirect:/user/contact";
        }

        Optional<Contact> contactOptional = contactService.findById(id);
        User user = (User) session.getAttribute("user");

        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            if (contact.getUser().getId().equals(user.getId())) {
                model.addAttribute("contact", contact);
                model.addAttribute("profilePic", "data:image/png;base64," + Base64.getEncoder().encodeToString(contact.getImg()));
                //session.setAttribute("message", new Message("alert-danger", "Unauthorized Contact Access"));
            } else {
                session.setAttribute("message", new Message("alert-danger", "Unauthorized Contact Access"));
            }
        } else {
            session.setAttribute("message", new Message("alert-danger", "Contact Not Found"));
        }

        return "user/viewContact";
    }

    @PostMapping("/user/delete")
    public String deleteContact(@RequestParam("id") Long id, Model model, Principal principal, HttpSession session) {
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        model.addAttribute("title", "All Contacts");

        if (id == null || id <= 0) {
            session.setAttribute("message", new Message("alert-danger", "Contact Not Found"));
            return "redirect:/user/all-contact";
        }

        Contact contact = contactService.findById(id).orElseThrow(() -> new RuntimeException("Contact Doesn't Exist"));

        if (user.getId().equals(contact.getUser().getId())) {
            user.getContactList().remove(contact);
            User save = userService.save(user);
            session.setAttribute("user", save);
            session.setAttribute("message", new Message("alert-success", "Contact Deleted Successfully"));
        } else {
            session.setAttribute("message", new Message("alert-danger", "Unauthorized Contact Access Attempt"));
        }

        return "redirect:/user/all-contact";
    }

    @PostMapping("/user/edit")
    public String editContact(@RequestParam("id") Long id, Principal principal, Model model, HttpSession session) {
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        model.addAttribute("title", "Edit Contact");

        if (id == null || id <= 0) {
            session.setAttribute("message", new Message("alert-danger", "Contact Not Found"));
            return "redirect:/user/all-contact";
        }
        Contact contact = contactService.findById(id).orElseThrow(() -> new RuntimeException("Contact Not Found"));
        if (contact.getUser().getId().equals(user.getId())) {
            model.addAttribute("contact", contact);
            model.addAttribute("profilePic", "data:image/png;base64," + Base64.getEncoder().encodeToString(contact.getImg()));
            return "user/editContact";
        } else {
            session.setAttribute("message", new Message("alert-danger", "Unauthorized Contact Access"));
            return "redirect:/user/all-contact";
        }
    }

    @PostMapping("/user/update-contact")
    public String updateContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
                                @RequestParam("profimg") MultipartFile img,
                                Model model, HttpSession session) {

        model.addAttribute("title", "Edit Contact");

        if (result.hasErrors()) {
            return "user/editContact";
        }

        try {
            Contact org = contactService.findById(contact.getId()).orElseThrow(() -> new Exception("Contact Not Found"));
            if (img.isEmpty()) {
                contact.setImg(org.getImg());
            } else {
                contact.setImg(img.getBytes());
            }
            contact.setUser(org.getUser());
            Contact save = contactService.save(contact);

            model.addAttribute("title", "View Contact");

            if (save != null) {
                session.setAttribute("message", new Message("alert-success", "Contact Updated Successfully"));
            } else {
                session.setAttribute("message", new Message("alert-danger", "Contact Updation Failed"));
            }
            return "redirect:/user/contact/" + contact.getId();
        } catch (Exception e) {
            session.setAttribute("message", new Message("alert-danger", "Error: " + e.getMessage()));
            return "user/editContact";
        }
    }
}
