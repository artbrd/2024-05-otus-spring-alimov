package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorConverter authorConverter;

    private final OtherLibraryService otherLibraryService;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "authorBreaker", fallbackMethod = "otherLibraryAuthorsFallbackMethod")
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorConverter::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return authorRepository.count();
    }

    public List<AuthorDto> otherLibraryAuthorsFallbackMethod(Throwable ex) {
        log.error("List author error", ex);
        return otherLibraryService.getAuthors();
    }
}
