package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class AuthorRepositoryTest {

    private static final String AUTHOR_ID_1 = "1";
    private static final String AUTHOR_ID_2 = "2";
    private static final String AUTHOR_ID_3 = "3";
    private static final String AUTHOR_ID_100 = "100";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    @DisplayName("должен загружать список всех авторов")
    void shouldReturnCorrectAuthorsList() {
        var author1 = mongoOperations.findById(AUTHOR_ID_1, Author.class);
        var author2 = mongoOperations.findById(AUTHOR_ID_2, Author.class);
        var author3 = mongoOperations.findById(AUTHOR_ID_3, Author.class);

        var actualAuthors = authorRepository.findAll();

        assertThat(actualAuthors).containsExactly(author1, author2, author3);
    }

    @Test
    @DisplayName("должен загружать автора по id")
    void shouldReturnCorrectAuthorById() {
        var expected = mongoOperations.findById(AUTHOR_ID_1, Author.class);
        var actual = authorRepository.findById(AUTHOR_ID_1);

        assertThat(actual).isPresent().hasValue(expected);
    }

    @Test
    @DisplayName("должен не загрузить автора по несуществующему id")
    void shouldReturnEmptyAuthorById() {
        var actual = authorRepository.findById(AUTHOR_ID_100);

        assertThat(actual).isNotPresent();
    }
}