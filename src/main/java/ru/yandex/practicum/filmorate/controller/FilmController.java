package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Количество фильмов в базе данных: {}", filmService.getFilms().size());
        return filmService.getFilms();
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Запрос на внесения фильма в базу: {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Запрос на обновление данных фильма: {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Запрос на добавление лайка к фильму: {}", filmService.getFilmById(id));
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Запрос на удаление лайка по фильму: {}", filmService.getFilmById(id));
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Запрос на получение популярных фильмов в кол-ве: {}", count);
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/films/{filmId}")
    public Film findFilm(@PathVariable("filmId") Long filmId) {
        log.info("Запрос на получение фильма по id: {}", filmId);
        return filmService.getFilmById(filmId);
    }
}