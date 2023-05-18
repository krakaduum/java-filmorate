package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    Map<Integer, Film> films = new HashMap<>();

    @Override
    public void addFilm(Film film) {
        if (films.containsKey(film.getId())) {
            String filmAlreadyExists = "Фильм с таким id уже существует";
            log.error(filmAlreadyExists);
            throw new RuntimeException(filmAlreadyExists);
        }

        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            String filmDoesNotExist = "Фильма с таким id не существует";
            log.error(filmDoesNotExist);
            throw new NoSuchElementException(filmDoesNotExist);
        }

        films.put(film.getId(), film);
    }

    @Override
    public void deleteFilm(int id) {
        if (!films.containsKey(id)) {
            String filmDoesNotExist = "Фильма с таким id не существует";
            log.error(filmDoesNotExist);
            throw new NoSuchElementException(filmDoesNotExist);
        }

        films.remove(id);
    }

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            String filmDoesNotExist = "Фильма с таким id не существует";
            log.error(filmDoesNotExist);
            throw new NoSuchElementException(filmDoesNotExist);
        }

        return films.get(id);
    }

    @Override
    public Set<Film> getFilms() {
        return films.values().stream().collect(Collectors.toSet());
    }
}
