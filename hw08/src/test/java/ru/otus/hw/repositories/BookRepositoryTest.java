package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@DataMongoTest
class BookRepositoryTest {

    private static final String GENRE_ID_1 = "1";
    private static final String GENRE_ID_3 = "3";
    private static final String AUTHOR_ID_1 = "1";
    private static final String BOOK_ID_1 = "1";
    private static final String BOOK_ID_2 = "2";
    private static final String BOOK_ID_3 = "3";
    private static final String BOOK_ID_100 = "100";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    @DisplayName("должен сохранять новую книгу")
    void shouldSaveNewBook() {
        var author = mongoOperations.findById(AUTHOR_ID_1, Author.class);
        var genre = mongoOperations.findById(GENRE_ID_1, Genre.class);
        var bookToSave = new Book(null, "BookTitle_10500", author, List.of(genre));

        var savedBook = bookRepository.save(bookToSave);
        assertThat(savedBook).isNotNull()
                .matches(b -> b.getId() != null)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(bookToSave);

        var foundBook = mongoOperations.findById(savedBook.getId(), Book.class);
        assertThat(foundBook).isEqualTo(savedBook);
    }

    @Test
    @DisplayName("должен сохранять измененную книгу")
    void shouldSaveUpdatedBook() {
        var updatedTitle = "BookTitle_10500";
        var existingBook = mongoOperations.findById(BOOK_ID_1, Book.class);
        var additionalGenre = mongoOperations.findById(GENRE_ID_3, Genre.class);

        var bookToUpdate = new Book(existingBook.getId(), updatedTitle, existingBook.getAuthor(), List.of(additionalGenre));
        Book updated = bookRepository.save(bookToUpdate);

        assertThat(updated).isNotNull()
                .matches(b -> b.getTitle().equals(updatedTitle))
                .matches(b -> b.getGenres().size() == 1)
                .matches(b -> b.getGenres().contains(additionalGenre));

        assertThat(mongoOperations.findById(updated.getId(), Book.class)).isEqualTo(updated);
    }

    @Test
    @DisplayName("должен удалять книгу по id")
    void shouldDeleteBook() {
        var bookToDelete = mongoOperations.findById(BOOK_ID_1, Book.class);
        assertThat(bookToDelete).isNotNull();

        bookRepository.deleteById(BOOK_ID_1);
        assertThat(mongoOperations.findById(BOOK_ID_1, Book.class)).isNull();
    }

    @Nested
    @DirtiesContext(classMode = BEFORE_CLASS)
    class BookRepositoryFindTest {
        @Test
        @DisplayName("должен загружать книгу по id")
        void shouldReturnCorrectBookById() {
            var expected = mongoOperations.findById(BOOK_ID_1, Book.class);
            var actual = bookRepository.findById(BOOK_ID_1);

            assertThat(actual).isPresent()
                    .hasValue(expected);
        }

        @Test
        @DisplayName("должен не загрузить комментарий по несуществующему id")
        void shouldReturnEmptyBookById() {
            var actual = bookRepository.findById(BOOK_ID_100);

            assertThat(actual).isNotPresent();
        }

        @Test
        @DisplayName("должен загружать список всех книг")
        void shouldReturnCorrectBooksList() {
            var book1 = mongoOperations.findById(BOOK_ID_1, Book.class);
            var book2 = mongoOperations.findById(BOOK_ID_2, Book.class);
            var book3 = mongoOperations.findById(BOOK_ID_3, Book.class);

            var actual = bookRepository.findAll();

            assertThat(actual).containsExactly(book1, book2, book3);
        }
    }
}