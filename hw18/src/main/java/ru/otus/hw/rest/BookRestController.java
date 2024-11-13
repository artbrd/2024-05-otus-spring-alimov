package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.services.BookService;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @GetMapping(value = "api/v1/books")
    public Flux<BookDto> getAllBook() {
        return bookService.findAll();
    }

    @PostMapping(value = "api/v1/books")
    public Mono<BookDto> saveBook(@RequestBody BookEditDto book) {
        return bookService.insert(book.getTitle(), book.getAuthor(), book.getGenre());
    }

    @DeleteMapping("/api/v1/books/{id}")
    public Mono<Void> deleteBook(@PathVariable("id") String id) {
        return bookService.deleteById(id);
    }

    @PutMapping("/api/v1/books/{id}")
    public Mono<BookDto> updateBookById(@PathVariable("id") String id, @RequestBody BookEditDto bookEditDto) {
        return bookService.update(id, bookEditDto.getTitle(), bookEditDto.getAuthor(), bookEditDto.getGenre());
    }
}
