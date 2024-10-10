package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    @Override
    @Transactional(readOnly = true)
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id).map(bookConverter::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookConverter::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookDto insert(String title, long authorId, long genre) {
        return bookConverter.toDto(save(0, title, authorId, genre));
    }

    @Override
    @Transactional
    public BookDto update(long id, String title, long authorId, long genre) {
        return bookConverter.toDto(save(id, title, authorId, genre));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, long genre) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var maybeGenre = genreRepository.findById(genre)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(authorId)));
        var book = new Book(id, title, author, maybeGenre);
        return bookRepository.save(book);
    }
}
