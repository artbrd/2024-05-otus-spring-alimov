package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto insert(String title, long authorId, long genre);

    BookDto update(long id, String title, long authorId, long genre);

    void deleteById(long id);

    long count();
}
