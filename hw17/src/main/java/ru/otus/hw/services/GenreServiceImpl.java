package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreConverter genreConverter;

    private final OtherLibraryService otherLibraryService;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "genreBreaker", fallbackMethod = "otherLibraryGenresFallbackMethod")
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(genreConverter::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return genreRepository.count();
    }

    public List<GenreDto> otherLibraryGenresFallbackMethod(Throwable ex) {
        log.error("List genre error", ex);
        return otherLibraryService.getGenres();
    }
}
