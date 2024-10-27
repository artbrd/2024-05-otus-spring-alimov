package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;

    @CircuitBreaker(name = "authorCircuitBreaker", fallbackMethod = "emptyAuthorListFallbackMethod")
    @GetMapping(value = "api/v1/authors", produces = APPLICATION_JSON_VALUE)
    public List<AuthorDto> getAllAuthor() {
        return authorService.findAll();
    }

    public List<AuthorDto> emptyAuthorListFallbackMethod(Exception ex) {
        log.error("List author error", ex);
        return new ArrayList<>();
    }
}
