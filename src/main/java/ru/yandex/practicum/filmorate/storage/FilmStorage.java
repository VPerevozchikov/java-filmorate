package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    void addFilm(Film film);

    void deleteFilm(Film film);

    Map<Long, Film> getFilms();

}