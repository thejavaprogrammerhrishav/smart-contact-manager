package com.smart.contact.contacts.controller;

import com.smart.contact.contacts.service.ContactService;
import com.smart.contact.user.model.User;
import com.smart.contact.user.service.UserService;
import com.smart.contact.util.Csv;
import com.smart.contact.util.Excel;
import com.smart.contact.util.HTMLExporter;
import com.smart.contact.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.concurrent.ExecutorService;

@Controller
public class ExportController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    private void common(Model model, Principal principal, HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null && principal != null) {
            System.out.println("Fetched User.....");
            user = userService.findByUsername(principal.getName()).orElse(new User());
            session.setAttribute("user", (User) user);
        }
    }

    @GetMapping("/user/export")
    public String export(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User Not Found.."));
        model.addAttribute("user", user);
        model.addAttribute("title", "Export Contacts");
        return "user/export";
    }

    @GetMapping("/user/download/html")
    public void download(Principal principal, HttpSession session, Model model, HttpServletResponse response) {
        model.addAttribute("title", "Export Contact");
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        try {
            response.setContentType("application/octet-stream");
            String headerValue = "attachment; filename=" + user.getUsername() + "_Contact Data.html";
            response.setHeader("Content-Disposition", headerValue);
            String data = HTMLExporter.getInstance(user).download();
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(data.getBytes(StandardCharsets.UTF_8));
            outputStream.close();
        } catch (Exception e) {
            session.setAttribute("message", new Message("bg-danger", "Error: " + e.getMessage()));
            try {
                response.sendRedirect("/user/export");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @GetMapping("/user/download/csv")
    public void downloadCsv(Principal principal, HttpSession session, Model model, HttpServletResponse response) {
        model.addAttribute("title", "Export Contact");
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        try {
            response.setContentType("application/octet-stream");
            String headerValue = "attachment; filename=" + user.getUsername() + "_Contact Data.csv";
            response.setHeader("Content-Disposition", headerValue);
            ServletOutputStream outputStream = response.getOutputStream();
            Csv.getCsv(user, outputStream);
            outputStream.close();
        } catch (Exception e) {
            session.setAttribute("message", new Message("bg-danger", "Error: " + e.getMessage()));
            try {
                response.sendRedirect("/user/export");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @GetMapping("/user/download/excel/xls")
    public void downloadExcelXls(Principal principal, HttpSession session, Model model, HttpServletResponse response) {
        excelExport("xls", principal, session, model, response);
    }

    @GetMapping("/user/download/excel/xlsx")
    public void downloadExcelXlsx(Principal principal, HttpSession session, Model model, HttpServletResponse response) {
        excelExport("xlsx", principal, session, model, response);
    }

    private void excelExport(String type, Principal principal, HttpSession session, Model model, HttpServletResponse response) {
        model.addAttribute("title", "Export Contact");
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("user", user);
        try {
            response.setContentType("application/octet-stream");
            String headerValue = "attachment; filename=" + user.getUsername() + "_Contact Data." + type;
            response.setHeader("Content-Disposition", headerValue);
            ServletOutputStream outputStream = response.getOutputStream();
            if (type.equals("xls")) {
                Excel.excelXls(user, outputStream);
            } else {
                Excel.excelXlsx(user, outputStream);
            }
            outputStream.close();
        } catch (Exception e) {
            session.setAttribute("message", new Message("bg-danger", "Error: " + e.getMessage()));
            try {
                response.sendRedirect("/user/export");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
