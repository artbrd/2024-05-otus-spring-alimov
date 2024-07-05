package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Genre> findAll() {
        try {
            return namedParameterJdbcOperations.query("SELECT id, name FROM genres",
                    new EmptySqlParameterSource(),
                    new GnreRowMapper());
        } catch (DataAccessException dae) {
            log.error("Exception:", dae);
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Genre> findById(long id) {
        try {
            var params = Map.of("id", id);
            var genre = namedParameterJdbcOperations.queryForObject("SELECT id, name FROM genres WHERE id = :id",
                    params,
                    new GnreRowMapper());
            return Optional.of(genre);
        } catch (DataAccessException dae) {
            log.error("Exception:", dae);
            return Optional.empty();
        }
    }

    private static class GnreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
