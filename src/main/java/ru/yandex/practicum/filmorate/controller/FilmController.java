package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
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

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Запрос на внесение фильма в базу: {}", film);
        return filmService.addFilm(film);
    }

    @DeleteMapping("/films/{filmId}")
    public boolean deleteFilm(@PathVariable("filmId") Long filmId) {
        log.info("Запрос на удаление фильма по id: {}", filmId);
        return filmService.deleteFilmById(filmId);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Запрос на обновление фильма в базу: {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/films/{filmId}")
    public Film findFilm(@PathVariable("filmId") Long filmId) {
        log.info("Запрос на получение фильма по id: {}", filmId);
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Запрос на добавление лайка к фильму по id: {}", id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Запрос на добавление лайка к фильму по id: {}", id);
        filmService.deleteLike(id, userId);
    }

    @PutMapping("/films/{id}/genre/{genreId}")
    public void addGenre(@PathVariable("id") Long id, @PathVariable("genreId") Long genreId) {
        log.info("Запрос на добавление жанра к фильму по id: {}", id);
        filmService.addGenre(id, genreId);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Запрос на получение всех фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("Запрос на получение популярных фильмов в кол-ве: {}", count);
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/genres/{genreId}")
    public Genre getGenreById(@PathVariable("genreId") Long genreId) {
        log.info("Запрос на получение жанра по id: {}", genreId);
        return filmService.getGenreById(genreId);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        log.info("Запрос на получение всех жанров");
        return filmService.getGenres();
    }

    @GetMapping("/mpa/{ratingMpaId}")
    public Mpa getRatingMpaById(@PathVariable("ratingMpaId") Long ratingMpaId) {
        log.info("Запрос на получение рейтинга MPA по id: {}", ratingMpaId);
        return filmService.getMpaById(ratingMpaId);
    }

    @GetMapping("/mpa")
    public List<Mpa> getRatingsMpa() {
        log.info("Запрос на получение всех рейтингов MPA");
        return filmService.getAllMpa();
    }
}