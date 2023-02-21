package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private Long filmId = 0L;
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    public static final LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) throws ValidationException {
        validate(film);
        film.setId(++filmId);
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) throws ValidationException {
        validate(film);
        if (filmStorage.getFilms().containsKey(film.getId())) {
            filmStorage.addFilm(film);
        } else {
            throw new FilmNotFoundException(String.format(
                    "Фильм c id %s не найден", film.getId()));
        }
        return film;
    }

    public void deleteFilm(Film film) {
        if (filmStorage.getFilms().containsKey(film.getId())) {
            filmStorage.deleteFilm(film);
        } else {
            throw new FilmNotFoundException(String.format(
                    "Фильм c id %s не найден", film.getId()));
        }
    }

    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public Film getFilmById(Long id) {
        if (filmStorage.getFilms().containsKey(id)) {
            return filmStorage.getFilms().get(id);
        } else {
            throw new FilmNotFoundException(String.format(
                    "Фильм c id %s не найден", id));
        }
    }

    public void putLike(Long idFilm, Long idUser) {
        if (filmStorage.getFilms().containsKey(idFilm)) {
            if (userStorage.getUsers().containsKey(idUser)) {
                getFilmById(idFilm).listOfLikes.add(idUser);
            } else {
                throw new UserNotFoundException(String.format(
                        "Пользователь c id %s не найден", idUser));
            }
        } else {
            throw new FilmNotFoundException(String.format(
                    "Фильм c id %s не найден", idFilm));
        }
    }

    public void deleteLike(Long idFilm, Long idUser) {
        if (filmStorage.getFilms().containsKey(idFilm)) {
            if (userStorage.getUsers().containsKey(idUser)) {
                getFilmById(idFilm).listOfLikes.remove(idUser);
            } else {
                throw new UserNotFoundException(String.format(
                        "Пользователь c id %s не найден", idUser));
            }
        } else {
            throw new FilmNotFoundException(String.format(
                    "Фильм c id %s не найден", idFilm));
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count > 0) {
            FilmPopularComparator filmPopularComparator = new FilmPopularComparator();
            return filmStorage.getFilms().values().stream()
                    .sorted(filmPopularComparator)
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            throw new IncorrectParameterException("count");
        }
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