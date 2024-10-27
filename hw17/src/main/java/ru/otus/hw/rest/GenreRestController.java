package ru.otus.hw.rest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreRestController {

    private final GenreService genreService;

    @CircuitBreaker(name = "genreCircuitBreaker", fallbackMethod = "emptyGenreListFallbackMethod")
    @GetMapping(value = "api/v1/genres", produces = APPLICATION_JSON_VALUE)
    public List<GenreDto> getAllGenre() {
        return genreService.findAll();
    }

    public List<GenreDto> emptyGenreListFallbackMethod(Exception ex) {
        log.error("List genre error", ex);
        return new ArrayList<>();
    }
}
