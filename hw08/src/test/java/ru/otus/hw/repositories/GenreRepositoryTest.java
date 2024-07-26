package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.models.Genre;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class GenreRepositoryTest {

    private static final String GENRE_ID_1 = "1";
    private static final String GENRE_ID_2 = "2";
    private static final String GENRE_ID_3 = "3";
    private static final String GENRE_ID_4 = "4";
    private static final String GENRE_ID_5 = "5";
    private static final String GENRE_ID_100 = "100";
    private static final String GENRE_ID_101 = "101";
    private static final String GENRE_ID_102 = "102";

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    @DisplayName("должен загружать список всех жанров")
    void shouldReturnCorrectGenresList() {
        var genre1 = mongoOperations.findById(GENRE_ID_1, Genre.class);
        var genre2 = mongoOperations.findById(GENRE_ID_2, Genre.class);
        var genre3 = mongoOperations.findById(GENRE_ID_3, Genre.class);
        var genre4 = mongoOperations.findById(GENRE_ID_4, Genre.class);
        var genre5 = mongoOperations.findById(GENRE_ID_5, Genre.class);

        var actual = genreRepository.findAll();

        assertThat(actual).containsExactly(genre1, genre2, genre3, genre4, genre5);
    }

    @Test
    @DisplayName("должен загружать жанры по id")
    void shouldReturnCorrectGenreById() {
        var genre1 = mongoOperations.findById(GENRE_ID_1, Genre.class);
        var genre2 = mongoOperations.findById(GENRE_ID_2, Genre.class);
        var genre3 = mongoOperations.findById(GENRE_ID_3, Genre.class);
        var genres = Set.of(GENRE_ID_1, GENRE_ID_2, GENRE_ID_3,
                GENRE_ID_100, GENRE_ID_101, GENRE_ID_102);

        var actual = genreRepository.findAllById(genres);

        assertThat(actual).containsExactly(genre1, genre2, genre3);
    }
}