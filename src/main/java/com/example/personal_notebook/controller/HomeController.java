package com.example.personal_notebook.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session) {
        Object securityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null) {
            return "redirect:/notes.html";
        }
        return "redirect:/login.html";
    }
}