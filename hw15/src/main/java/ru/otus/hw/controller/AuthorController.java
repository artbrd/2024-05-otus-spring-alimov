package ru.otus.hw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorController {

    @GetMapping("/authors")
    public String getAllAuthor() {
        return "authors/allAuthors";
    }
}
