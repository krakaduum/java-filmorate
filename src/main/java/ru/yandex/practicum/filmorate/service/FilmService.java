package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Set;

@Service
@Slf4j
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);

        log.info("Добавлен лайк пользователя " + userId + " к фильму " + filmId);
    }

    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);

        log.info("Удален лайк пользователя " + userId + " к фильму " + filmId);
    }

    public Set<Film> getTopFilms(int count) {
        return filmStorage.getTopFilms(count);
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public void addFilm(Film film) {
        filmStorage.addFilm(film);

        log.info("Добавлен фильм с идентификатором " + film.getId());
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }
}
