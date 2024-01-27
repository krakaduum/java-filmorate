package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Qualifier("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    Map<Integer, Film> films = new HashMap<>();

    @Override
    public void addFilm(Film film) {
        if (films.containsKey(film.getId())) {
            String filmAlreadyExists = "Фильм с id = " + film.getId() + " уже существует";
            throw new RuntimeException(filmAlreadyExists);
        }

        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            String filmDoesNotExist = "Фильма с id = " + film.getId() + " не существует";
            throw new NoSuchElementException(filmDoesNotExist);
        }

        films.put(film.getId(), film);
    }

    @Override
    public void deleteFilm(int id) {
        if (!films.containsKey(id)) {
            String filmDoesNotExist = "Фильма с id = " + id + " не существует";
            throw new NoSuchElementException(filmDoesNotExist);
        }

        films.remove(id);
    }

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            String filmDoesNotExist = "Фильма с id = " + id + " не существует";
            throw new NoSuchElementException(filmDoesNotExist);
        }

        return films.get(id);
    }

    @Override
    public Set<Film> getFilms() {
        return films.values().stream().collect(Collectors.toSet());
    }

    public void addLike(int filmId, int userId) {
        Film film = getFilm(filmId);
        if (film == null) {
            throw new NoSuchElementException("Фильм с id = " + filmId + " не найден");
        }
        film.addLike(userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = getFilm(filmId);
        if (film == null) {
            throw new NoSuchElementException("Фильм с id = " + filmId + " не найден");
        }

        if (!film.getLikes().contains(userId)) {
            throw new NoSuchElementException("Лайк пользователя " + userId + " не найден");
        }

        film.removeLike(userId);
    }

    public Set<Film> getTopFilms(int count) {
        return getFilms().stream()
                .sorted(Comparator.comparingInt(x -> -x.getLikes().size()))
                .limit(count)
                .collect(Collectors.toSet());
    }
}
