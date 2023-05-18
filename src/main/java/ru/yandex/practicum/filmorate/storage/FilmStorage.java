package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

public interface FilmStorage {

    public void addFilm(Film film);

    public void updateFilm(Film film);

    public void deleteFilm(int id);

    public Film getFilm(int id);

    public Set<Film> getFilms();
}
