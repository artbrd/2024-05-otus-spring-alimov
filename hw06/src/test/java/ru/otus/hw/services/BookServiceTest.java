package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("Сервис для работы с книгами ")
@DataJpaTest
@Import({BookServiceImpl.class, JpaAuthorRepository.class, JpaGenreRepository.class, JpaBookRepository.class})
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class BookServiceTest {

    @Autowired
    BookService bookService;

    @DisplayName("должен найти книгу по id")
    @Test
    void shouldFindBookById() {
        var actualBook = bookService.findById(1L);
        assertAll(
                () -> assertThat(actualBook).isPresent(),
                () -> assertThat(actualBook.get().getId()).isEqualTo(1L),
                () -> assertThat(actualBook.get().getTitle()).isEqualTo("BookTitle_1"),
                () -> assertThat(actualBook.get().getAuthor()).isNotNull(),
                () -> assertThat(actualBook.get().getAuthor().getId()).isEqualTo(1L),
                () -> assertThat(actualBook.get().getGenres()).isNotEmpty().hasSize(2)
        );
    }

    @DisplayName("должен найти все книги")
    @Test
    void shouldFindAllBooks() {
        var actualBooks = bookService.findAll();
        assertThat(actualBooks)
                .isNotEmpty()
                .hasSize(3);
    }

    @DisplayName("должен добавлять новую книгу")
    @Test
    void shouldAddNewBook() {
        var actualBook = bookService.insert("New title", 1L, Set.of(1L));
        var expectedBook = bookService.findById(actualBook.getId());
        assertThat(actualBook)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook.get());
    }

    @DisplayName("должен обновлять книгу")
    @Test
    void shouldUpdateBook() {
        var book = bookService.findById(1L);
        var genreIds = book.get()
                .getGenres()
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        var actualBook = bookService.update(book.get().getId(), "New title", book.get().getAuthor().getId(), genreIds);
        var expectedBook = bookService.findById(1L);
        assertThat(actualBook)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook.get());
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBookById() {
        bookService.deleteById(1L);
        var actualBook = bookService.findById(1L);
        assertThat(actualBook).isEmpty();
    }
}
