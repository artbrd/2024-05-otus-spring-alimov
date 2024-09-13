package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookConverter bookConverter;

    @GetMapping("/")
    public String getAllBook(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);
        return "books/allBooks";
    }

    @GetMapping(value = "/create")
    public String createBook(Model model) {
        var authors = authorService.findAll();
        var genres = genreService.findAll();

        model.addAttribute("book", new BookEditDto());
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);

        return "books/createBook";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable("id") long id, Model model) {
        var book = bookService.findById(id)
                .map(bookConverter::toBookEditDto)
                .orElseThrow(NotFoundException::new);
        var authors = authorService.findAll();
        var genres = genreService.findAll();

        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);

        return "books/editBook";
    }

    @PostMapping("/edit")
    public String updateBook(@Valid @ModelAttribute("book") BookEditDto book) {
        bookService.update(book.getId(), book.getTitle(), book.getAuthor(), book.getGenre());
        return "redirect:/";
    }

    @PostMapping(value = "/save")
    public String saveBook(@Valid @ModelAttribute("book") BookEditDto book, Model model) {
        bookService.insert(book.getTitle(), book.getAuthor(), book.getGenre());

        model.addAttribute("books", bookService.findAll());

        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);

        return "redirect:/";
    }
}
