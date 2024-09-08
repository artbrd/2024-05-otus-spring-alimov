package ru.otus.hw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @PostMapping("/login-fail")
    public String failPage() {
        return "login/login-fail";
    }

    @GetMapping("/forbidden")
    public String forbidden() {
        return "login/forbidden";
    }
}
