package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class LikesDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Collection<Integer> getLikesForFilm(int filmId) {
        String sqlQuery = "select user_id from likes where film_id=?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("user_id"), filmId);
    }

    public void addLike(int filmId, int userId) {
        String sqlQuery = "insert into likes (film_id, user_id) values(?,?)";

        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        if (!getLikesForFilm(filmId).contains(userId)) {
            throw new NoSuchElementException("Пользователь с id = " + userId + " не найден");
        }

        String sqlQuery = "delete from likes where user_id=? and film_id=?";

        jdbcTemplate.update(sqlQuery, userId, filmId);
    }
}
