package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(String id);

    List<CommentDto> findAllByBookId(String bookId);

    CommentDto insert(String text, String bookId);

    CommentDto update(String id, String text, String bookId);

    void deleteById(String id);
}
