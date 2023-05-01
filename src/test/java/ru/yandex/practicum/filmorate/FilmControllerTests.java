package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTests {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        film = new Film();
    }
    
    @Test
    void shouldNotBeValidWhenEmptyName() {
        film.setId(0);
        film.setName("");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(2020, 10, 20));
        film.setDuration(90);

        Exception exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Валидация не пройдена", exception.getMessage());
    }

    @Test
    void shouldNotBeValidWhenDescriptionIsMoreThan200() {
        film.setId(0);
        film.setName("name");
        film.setDescription("descdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdesc" +
                "descdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdesc" +
                "descdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdescdesc");
        film.setReleaseDate(LocalDate.of(2020, 10, 20));
        film.setDuration(90);

        Exception exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Валидация не пройдена", exception.getMessage());
    }

    @Test
    void shouldNotBeValidWhenDurationIs0() {
        film.setId(0);
        film.setName("name");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(2020, 10, 20));
        film.setDuration(0);

        Exception exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Валидация не пройдена", exception.getMessage());
    }

    @Test
    void shouldNotBeValidWhenWrongDate() {
        film.setId(0);
        film.setName("");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(1900, 10, 20));
        film.setDuration(90);

        Exception exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Валидация не пройдена", exception.getMessage());
    }

    @Test
    void shouldBeValidWhenDateIs28December1895() {
        film.setId(0);
        film.setName("name");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(90);
        filmController.addFilm(film);

        assertEquals(1, filmController.getFilms().size());
    }

    @Test
    void shouldBeValidWhenDescriptionIs200() {
        film.setId(0);
        film.setName("name");
        film.setDescription("descrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescr" +
                "descrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescrdescr" +
                "descrdescrdescr");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(90);
        filmController.addFilm(film);

        assertEquals(1, filmController.getFilms().size());
    }
}
