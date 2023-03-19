package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;


public interface FilmStorage {
    Film addFilm(Film film);

    boolean deleteFilmById(Long id);

    Film updateFilm(Film film);

    Film getFilmById(Long id);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    void addGenre(Long filmId, Long genreId);

    List<Film> getFilms();

    List<Film> getPopularFilms(Integer count);

    Genre getGenreById(Long id);

    List<Genre> getGenres();

    Mpa getMpaById(Long id);

    List<Mpa> getAllMpa();
}