package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedParameterJdbc;

    @Override
    public Optional<Book> findById(long id) {
        try {
            var params = Map.of("id", id);
            var book = namedParameterJdbc.queryForObject("SELECT b.id, b.title, a.id, a.full_name, g.id, g.name " +
                            "FROM books b " +
                            "JOIN authors a ON b.author_id = a.id " +
                            "JOIN genres g ON b.genre_id = g.id " +
                            "WHERE b.id = :id",
                    params,
                    new BookRowMapper());
            return Optional.of(book);
        } catch (DataAccessException dae) {
            log.error("Exception:", dae);
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        try {
            return namedParameterJdbc.query("SELECT b.id, b.title, a.id, a.full_name, g.id, g.name " +
                            "FROM books b " +
                            "JOIN authors a ON b.author_id = a.id " +
                            "JOIN genres g ON b.genre_id = g.id",
                    new EmptySqlParameterSource(),
                    new BookRowMapper());
        } catch (DataAccessException dae) {
            log.error("Exception:", dae);
            return Collections.emptyList();
        }
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        try {
            var params = Map.of("id", id);
            namedParameterJdbc.update("DELETE FROM books WHERE id = :id", params);
        } catch (DataAccessException dae) {
            log.error("Exception:", dae);
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        //...
        var parameterSource = new MapSqlParameterSource()
                .addValue("title", book.getTitle(), Types.VARCHAR)
                .addValue("authorId", book.getAuthor().getId(), Types.BIGINT)
                .addValue("genreId", book.getGenre().getId(), Types.BIGINT);
        namedParameterJdbc.update("INSERT INTO books " +
                        "(title, author_id, genre_id) " +
                        "VALUES (:title, :authorId, :genreId)",
                parameterSource,
                keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        var params = Map.of("id", book.getId(),
                "title", book.getTitle(),
                "authorId", book.getAuthor().getId(),
                "genreId", book.getGenre().getId());
        var updated = namedParameterJdbc.update("UPDATE books " +
                        "SET title = :title, author_id = :authorId, genre_id = :genreId" +
                        " WHERE id = :id",
                params);
        //...
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        if (updated == 0) {
            throw new EntityNotFoundException("Failed to update any rows");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long booksId = rs.getLong("books.id");
            String booksTitle = rs.getString("books.title");
            long authorsId = rs.getLong("authors.id");
            String authorsFullName = rs.getString("authors.full_name");
            long genresId = rs.getLong("genres.id");
            String genresName = rs.getString("genres.name");
            return new Book(booksId, booksTitle,
                    new Author(authorsId, authorsFullName),
                    new Genre(genresId, genresName));
        }
    }
}
