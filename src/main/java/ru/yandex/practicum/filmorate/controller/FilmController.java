package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int lastId = 1;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            String filmAlreadyExists = "Фильм с таким id уже существует";
            log.error(filmAlreadyExists);
            throw new ValidationException(filmAlreadyExists);
        }

        film.setId(lastId++);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        for (ConstraintViolation<Film> violation : violations) {
            log.error(violation.getMessage());
            throw new ValidationException("Валидация не пройдена");
        }

        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            String filmDoesNotExist = "Фильма с таким id не существует";
            log.error(filmDoesNotExist);
            throw new ValidationException(filmDoesNotExist);
        }
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        for (ConstraintViolation<Film> violation : violations) {
            log.error(violation.getMessage());
            throw new ValidationException("Валидация не пройдена");
        }
        films.put(film.getId(), film);
        return film;
    }
}
