package ru.otus.hw.repositories.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.SequenceNotFoundException;

import java.math.BigInteger;
import java.sql.Types;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SequenceRepositoryImpl implements SequenceRepository {

    private static final String NEX_VAL = "SELECT nextval(:name)";

    private final NamedParameterJdbcTemplate namedJdbcTemplate;


    @Override
    public long nextVal(String name) {
        var parameterSource = new MapSqlParameterSource().addValue("name", name, Types.VARCHAR);
        var nextVal = Optional.ofNullable(namedJdbcTemplate.queryForObject(NEX_VAL, parameterSource, BigInteger.class))
                .orElseThrow(() -> new SequenceNotFoundException(name));
        return nextVal.longValue();
    }
}
