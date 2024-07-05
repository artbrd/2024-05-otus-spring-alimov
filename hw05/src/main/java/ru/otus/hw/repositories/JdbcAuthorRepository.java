package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Author> findAll() {
        try {
            return namedParameterJdbcOperations.query("SELECT id, full_name FROM authors",
                    new EmptySqlParameterSource(),
                    new AuthorRowMapper());
        } catch (DataAccessException dae) {
            log.error("Exception:", dae);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            var params = Map.of("id", id);
            var author = namedParameterJdbcOperations.queryForObject("SELECT id, full_name FROM authors WHERE id = :id",
                    params,
                    new AuthorRowMapper());
            return Optional.of(author);
        } catch (DataAccessException dae) {
            log.error("Exception:", dae);
            return Optional.empty();
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
