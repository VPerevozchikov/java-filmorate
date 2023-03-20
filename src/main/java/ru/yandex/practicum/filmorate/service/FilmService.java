package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    public static final LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) throws ValidationException {
        validate(film);
        return filmStorage.addFilm(film);
    }

    public boolean deleteFilmById(Long id) {
        return filmStorage.deleteFilmById(id);
    }

    public Film updateFilm(Film film) throws ValidationException {
        validate(film);
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    public void addGenre(Long filmId, Long genreId) {
        filmStorage.addGenre(filmId, genreId);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }

    public Genre getGenreById(Long id) {
        return filmStorage.getGenreById(id);
    }

    public List<Genre> getGenres() {
        return filmStorage.getGenres();
    }

    public Mpa getMpaById(Long id) {
        return filmStorage.getMpaById(id);
    }

    public List<Mpa> getAllMpa() {
        return filmStorage.getAllMpa();
    }


    void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Поле 'name' пустое: {}", film.getName());
            throw new ValidationException("Ошибка в названии фильма.");
        }

        if (film.getDescription() == null
                || film.getDescription().isBlank()
                || film.getDescription().length() > 200) {
            log.info("Поле 'description' пустое или кол-во символов превышает 200: {}", film.getDescription());
            throw new ValidationException("Ошибка в описании фильма. Описание пустое или слишком длинное.");
        }

        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(FIRST_FILM)) {
            log.info("В поле 'releaseDate' введена недопустимо ранняя дата: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не верна.");
        }

        if (film.getDuration() <= 0) {
            log.info("Поле 'duration' ноль или отрицательное число: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма отрицательная или ноль.");
        }
    }
}