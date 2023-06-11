package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Qualifier("filmDbStorage")
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final LikesDbStorage likesDbStorage;

    @Override
    public void addFilm(Film film) {
        String sqlQuery = "insert into films (name, description, release_date, duration, mpa) " +
                "values (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        long idKey = keyHolder.getKey().longValue();
        film.setId((int) idKey);
    }

    @Override
    public void updateFilm(Film film) {
        String sqlQuery = "update films set name=?, description=?, release_date=?, duration=?, mpa=? " +
                "where id=?";
        int updatedFilms = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        updateGenresInFilm(film);
        film.setGenres(getGenresForFilm(film.getId()));
        if (updatedFilms == 0) {
            throw new NoSuchElementException("Фильм с id = " + film.getId() + " не найден");
        }
    }

    @Override
    public void deleteFilm(int id) {
        String sqlQuery = "delete from films where id=?";

        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Film getFilm(int id) {
        String sqlQuery = "select id, name, description, release_date, duration, mpa from films where id=?";
        try {
            Film film = jdbcTemplate.queryForObject(sqlQuery,
                    (ResultSet rs, int rowNum) -> createFilmObject(rs), id);
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Фильм с id = " + id + " не найден");
        }
    }

    @Override
    public Set<Film> getFilms() {
        String sqlQuery = "select id, name, description, release_date, duration, mpa from films";

        Set<Film> films =  jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                createFilmObject(rs)).stream().collect(Collectors.toSet());
        films.forEach(film -> film.setGenres(getGenresForFilm(film.getId())));
        return films;
    }

    @Override
    public void addLike(int filmId, int userId) {
        likesDbStorage.addLike(filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        likesDbStorage.removeLike(filmId, userId);
    }

    @Override
    public Set<Film> getTopFilms(int count) {
        String sqlQuery = "select films.*, count(l.film_id) as count from films " +
                "left join likes l on films.id=l.film_id " +
                "group by films.id " +
                "order by count desc " +
                "limit ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                createFilmObject(rs), count).stream().collect(Collectors.toSet());
    }

    private Film createFilmObject(ResultSet resultSet) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaDbStorage.getMpa(resultSet.getInt("mpa")))
                .genres(getGenresForFilm(resultSet.getInt("id")))
                .build();
    }

    private void updateGenresNamesForFilm(Film film) {
        var genres = genreDbStorage.getAllGenres();
        if (film.getGenres() != null) {
            film.getGenres().forEach(x -> x.setName(
                    genres.stream().filter(g -> x.getId() == g.getId()).findFirst().orElse(null)
                            .getName()));
        }
    }

    private void updateGenresInFilm(Film film) {
        if (film.getGenres() == null) {
            return;
        }

        String sqlDeleteQuery = "delete from film_genres where film_id=?";
        jdbcTemplate.update(sqlDeleteQuery, film.getId());

        film.getGenres().forEach(genre -> {
            String sqlQuery = "insert into film_genres(film_id, genre_id) values(?,?)";
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        });

        updateGenresNamesForFilm(film);
    }

    private Set<Genre> getGenresForFilm(int filmId) {
        String sqlQuery = "select id, name from genre g " +
                "left join film_genres fg on g.id=fg.genre_id where fg.film_id=?";

        try {
            TreeSet<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
            genres.addAll(jdbcTemplate.query(sqlQuery,
                    (ResultSet rs, int rowNum) -> genreDbStorage.createGenreObject(rs), filmId)
                    .stream().collect(Collectors.toSet()));
            return genres;
        } catch (RuntimeException e) {
            return new TreeSet<>();
        }
    }
}
