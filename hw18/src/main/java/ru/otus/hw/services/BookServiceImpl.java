package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    @Override
    public Mono<BookDto> findById(String id) {
        return bookRepository.findById(id).map(bookConverter::toDto);
    }

    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
                .map(bookConverter::toDto);
    }

    @Override
    public Mono<BookDto> insert(String title, String authorId, String genre) {
        return save(null, title, authorId, genre)
                .map(bookConverter::toDto);
    }

    @Override
    public Mono<BookDto> update(String id, String title, String authorId, String genre) {
        return save(id, title, authorId, genre)
                .map(bookConverter::toDto);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.deleteById(id);
    }

    private Mono<Book> save(String id, String title, String authorId, String genreId) {
        var authorMono = authorRepository.findById(authorId);
        var genreMono = genreRepository.findById(genreId);
        return Mono.zip(genreMono, authorMono, (genre, author) -> new Book(id, title, author, genre))
                .flatMap(bookRepository::save);
    }
}
