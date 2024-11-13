package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BookController {

    @GetMapping("/")
    public String getAllBook() {
        return "books/allBooks";
    }

    @GetMapping(value = "/create")
    public String createBook() {
        return "books/createBook";
    }

    @GetMapping("/edit")
    public String updateBook() {
        return "books/editBook";
    }

    @GetMapping("/delete")
    public String deleteBook() {
        return "books/deleteBook";
    }
}
