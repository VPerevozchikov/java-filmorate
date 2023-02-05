package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
public class FilmController extends AbstractController<Film> {
    public static final LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping("/films")
    public ArrayList getFilms() {
        log.info("Количество фильмов в базе данных: {}", data.size());
        return new ArrayList<>(data.values());
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if (film.getId() != null) {
            log.info("Запрос на обновление информации по существующему фильму в методе POST не корректен.");
            return null;
        }
        log.info("Запрос на внесения фильма в базу: {}", film);
        return createItem(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (film.getId() == null) {
            log.info("Запрос на создание нового фильма в методе PUT не корректен.");
            return null;
        }
        log.info("Запрос на обновление данных фильма: {}", film);
        return updateItem(film);
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