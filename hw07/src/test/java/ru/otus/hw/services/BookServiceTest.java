package ru.otus.hw.services;

import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DisplayName("Сервис для работы с книгами ")
@DataJpaTest
@Import({BookServiceImpl.class,
        BookConverter.class,
        AuthorConverter.class,
        GenreConverter.class})
@Transactional(propagation = Propagation.NEVER)
class BookServiceTest {

    @Autowired
    private BookService bookService;

    private List<AuthorDto> dbAuthors;

    private List<GenreDto> dbGenres;

    private List<BookDto> dbBooks;

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 6).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }

    private static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        var partition = ListUtils.partition(dbGenres, 2);
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id, "BookTitle_" + id, dbAuthors.get(id - 1), partition.get(id - 1)))
                .toList();
    }

    private static List<BookDto> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен добавлять новую книгу")
    @Test
    void shouldAddNewBook() {
        var expectedBook = new BookDto(dbBooks.size() + 1, "BookTitle_10",
                dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(1)));
        var actualBook = bookService.insert("BookTitle_10", dbAuthors.get(0).getId(),
                Set.of(dbGenres.get(0).getId(), dbGenres.get(1).getId()));
        assertThat(actualBook)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен обновлять книгу")
    @Test
    void shouldUpdateBook() {
        var actualBook = bookService.update(1L, "BookTitle_10", dbAuthors.get(0).getId(),
                Set.of(dbGenres.get(0).getId(), dbGenres.get(1).getId()));
        var expectedBook = new BookDto(1L, "BookTitle_10",
                dbAuthors.get(0),
                List.of(dbGenres.get(0), dbGenres.get(1)));
        assertThat(actualBook)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен удалять книгу по id")
    @Test
    void shouldDeleteBookById() {
        bookService.deleteById(1L);
        var actualBook = bookService.findById(1L);
        assertThat(actualBook).isEmpty();
    }

    @Nested
    @DirtiesContext(classMode = BEFORE_CLASS)
    class BookServiceFindTest {
        @DisplayName("должен найти книгу по id")
        @ParameterizedTest
        @MethodSource("ru.otus.hw.services.BookServiceTest#getDbBooks")
        void shouldFindBookById(BookDto expectedBookDto) {
            var actualBookDto = bookService.findById(expectedBookDto.getId());

            assertThat(actualBookDto).isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(expectedBookDto);
        }

        @DisplayName("должен найти все книги")
        @Test
        void shouldFindAllBooks() {
            var actualBooks = bookService.findAll();
            assertThat(actualBooks)
                    .isNotEmpty()
                    .hasSize(3);
        }
    }
}
