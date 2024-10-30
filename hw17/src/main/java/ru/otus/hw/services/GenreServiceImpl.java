package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
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

    private final CircuitBreaker circuitBreaker;

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {
        return circuitBreaker.run(() -> genreRepository.findAll()
                        .stream()
                        .map(genreConverter::toDto)
                        .toList(),
                this::otherLibraryGenresFallbackMethod);
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
