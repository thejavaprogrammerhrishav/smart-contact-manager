package com.smart.contact.user.controller;

import com.smart.contact.user.model.User;
import com.smart.contact.user.service.UserService;
import com.smart.contact.util.EmailSender;
import com.smart.contact.util.Forgot;
import com.smart.contact.util.Message;
import com.smart.contact.util.PasswordMatcher;
import com.smart.contact.util.Verification;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class ForgotController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private EmailSender sender;

    @GetMapping("/forgot")
    public String forgot(Model model, HttpServletRequest request) {
        model.addAttribute("title", "User Forgot Password");
        model.addAttribute("forgot", new Forgot("", ""));

        if (request.isUserInRole("ROLE_NORMAL")) {
            return "redirect:/loginSuccess";
        }
        return "forgot";
    }

    @PostMapping("/forgot")
    public String verifyUserData(@ModelAttribute("forgot") Forgot forgot, Model model, HttpSession session) {
        model.addAttribute("title", "Verify User Data | Reset Password");

        if (forgot.getUsername().isEmpty() || forgot.getEmail().isEmpty()) {
            session.setAttribute("message", new Message("alert-danger", "Please fill out fields"));
            return "redirect:/forgot";
        }

        Optional<User> fuser = userService.findByUsername(forgot.getUsername());
        if (!fuser.isPresent()) {
            session.setAttribute("message", new Message("alert-danger", "No user found with username: <b>" + forgot.getUsername() + "</b>"));
            return "redirect:/forgot";
        }

        User user = fuser.get();

        if (!user.getEmail().equals(forgot.getEmail())) {
            session.setAttribute("message", new Message("alert-danger", "User email doesn't match: " + forgot.getEmail()));
            return "redirect:/forgot";
        } else {
            session.setAttribute("forgotUser", user);
            Verification message = sender.message(user);
            if (message == null) {
                session.setAttribute("message", new Message("alert-danger", "Email Cannot Be Sent, Try Again Later"));
                return "redirect:/forgot";
            }
            session.setAttribute("verification", message);
            session.setAttribute("message", new Message("alert-success", "Reset Verification Email Sent Successfully"));
            return "redirect:/verify";
        }
    }

    @GetMapping("/verify")
    public String verify(Model model, HttpSession session) {
        model.addAttribute("title", "Verify User Data | Reset Password");

        Object v = session.getAttribute("verification");
        if (v == null) {
            session.setAttribute("message", new Message("alert-danger", "Nice Try, But Unauthorized Access"));
            return "redirect:/login";
        }

        return "resetOtp";
    }

    @PostMapping("/verify-otp-key")
    public String reset(@RequestParam("otp") String code, @RequestParam("verifcode") String verifcode,
                        Model model, HttpSession session) {
        model.addAttribute("title", "Reset Password");

        if (code.isEmpty() || verifcode.isEmpty()) {
            session.setAttribute("message", new Message("alert-danger", "Empty OTP Code or Verification Code"));
            return "redirect:/verify";
        }

        Verification verfication = (Verification) session.getAttribute("verification");

        if (!verfication.getCode().equals(code) && verfication.getUuid().equals(verifcode)) {
            session.setAttribute("message", new Message("alert-danger", "Entered Code Doesn't Match"));
            return "redirect:/verify";
        } else {
            return "redirect:/reset";
        }
    }

    @GetMapping("/reset")
    public String resetGet(Model model, HttpSession session) {
        model.addAttribute("title", "Reset Password");
        model.addAttribute("password", "");
        model.addAttribute("cpassword", "");

        Object v = session.getAttribute("verification");
        if (v == null) {
            session.setAttribute("message", new Message("alert-danger", "Nice Try, But Unauthorized Access"));
            return "redirect:/login";
        }
        return "reset";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("password") String password, @RequestParam("cpassword") String cpassword, HttpSession session) {
        if (password.equals(cpassword)) {
            if (PasswordMatcher.validate(password)) {
                session.removeAttribute("verification");

                User forgetUser = (User) session.getAttribute("forgotUser");
                forgetUser.setPassword(encoder.encode(password));
                User user = userService.save(forgetUser);
                session.removeAttribute("forgetUser");
                if (user.getId() > 0) {
                    session.setAttribute("message", new Message("alert-success", "Password Reset Successfully"));
                } else {
                    session.setAttribute("message", new Message("alert-danger", "Password Reset Failed"));
                }
                return "redirect:/login";
            } else {
                session.setAttribute("message", new Message("alert-danger", "Password Pattern Doesn't Match"));
            }
        } else {
            session.setAttribute("message", new Message("alert-danger", "Passwords Doesn't Match"));
        }
        return "redirect:/reset";
    }

    @PostMapping("/resend")
    public String resend(HttpSession session) {
        User user = (User) session.getAttribute("forgotUser");
        Verification message = sender.message(user);
        if (message == null) {
            session.setAttribute("message", new Message("alert-danger", "Email Cannot Be Sent, Try Again Later"));
        }
        session.setAttribute("verification", message);
        session.setAttribute("message", new Message("alert-success", "Reset Verification Email Sent Successfully"));
        return "redirect:/verify";
    }


    // Blocking
    @GetMapping(path = {"/verify-otp-key", "/reset-password", "/resend"})
    public String block1(HttpSession session) {
        session.setAttribute("message", new Message("alert-danger", "Nice Try, But Unauthorized Access"));
        return "redirect:/login";
    }

}
