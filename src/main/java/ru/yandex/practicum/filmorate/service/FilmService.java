package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NoSuchElementException("Фильм не найден");
        }
        film.addLike(userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NoSuchElementException("Фильм не найден");
        }

        if (!film.getLikes().contains(userId)) {
            throw new NoSuchElementException("Лайк не найден");
        }

        film.removeLike(userId);
    }

    public Set<Film> getTopFilms(int count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(x -> -x.getLikes().size()))
                .limit(count)
                .collect(Collectors.toSet());
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }
}
