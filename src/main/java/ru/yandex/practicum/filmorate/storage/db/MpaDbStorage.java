package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Mpa getMpa(int id) {
        String sqlQuery = "select id, name from mpa where id=?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery,
                    (ResultSet rs, int rowNum) -> createMpaObject(rs), id);
        } catch (RuntimeException e) {
            throw new NoSuchElementException("mpa " + id + " не найден");
        }
    }

    public Collection<Mpa> getAllMpas() {
        String sqlQuery = "select id, name from mpa";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> createMpaObject(rs));
    }

    private Mpa createMpaObject(ResultSet resultSet) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
