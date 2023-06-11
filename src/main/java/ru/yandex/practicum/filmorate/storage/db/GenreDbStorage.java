package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Genre getGenre(int id) {
        String sqlQuery = "select id, name from genre where id=?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery,
                    (ResultSet rs, int rowNum) -> createGenreObject(rs), id);
        } catch (RuntimeException e) {
            throw new NoSuchElementException("Жанр " + id + " не найден");
        }
    }

    public Collection<Genre> getAllGenres() {
        String sqlQuery = "select id, name from genre";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> createGenreObject(rs));
    }

    public Genre createGenreObject(ResultSet resultSet) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
