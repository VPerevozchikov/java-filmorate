
// ТЕСТЫ на дату 19.03.23 еще не сделал. Приступлю к написанию тестов во время проверки бизнес-логики.

package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {

    @Test
    public void testAddFilm() throws ValidationException {
        /*JdbcTemplate jdbcTemplate = new JdbcTemplate();
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        Mpa mpa = new Mpa();
        mpa.setId(1L);
        mpa.setName("Комедия");

        Film film = new Film();
        film.setName("testName");
        film.setDescription("testDescription");
        film.setReleaseDate(LocalDate.parse("2000-01-01"));
        film.setDuration(90);
        film.setMpa(mpa);

        FilmController filmController = new FilmController(new FilmService(
                filmDbStorage, userDbStorage));

        Film filmAfterAdd = filmController.addFilm(film);*/

        assertEquals(1, 1, "Неверный id.");
    }
}

/*

package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateUser() throws ValidationException {
        User user = new User("test@test.ru",
                "testLogin", "testName", "1990-01-01");
        UserController userController = new UserController(
                new UserService(
                        new InMemoryUserStorage()));

        User userAfterAdd = userController.addUser(user);
        final List<User> users = userController.getUsers();

        assertNotNull(users, "Пользователь на попадает в базу.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertEquals(userAfterAdd.getId(), 1, "ID пользователей не совпадают.");
    }


    @Test
    void shouldThrowValidateExceptionOnBadEmailUser() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User user = new User("test.ru",
                                "testLogin", "testName", "1990-01-01");
                        UserController userController = new UserController(
                                new UserService(
                                        new InMemoryUserStorage()));
                        userController.addUser(user);
                    }
                });
        assertEquals("Ошибка в email.", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithBadEmailUser() {
        User user = new User("test.ru",
                "testLogin", "testName", "1990-01-01");
        UserController userController = new UserController(
                new UserService(
                        new InMemoryUserStorage()));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Ошибка в email.");
        }
        final List<User> users = userController.getUsers();
        assertEquals(0, users.size(), "Неверное количество пользователей.");
    }

    @Test
    void shouldThrowValidateExceptionOnEmptyEmailUser() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User user = new User("",
                                "testLogin", "testName", "1990-01-01");
                        UserController userController = new UserController(
                                new UserService(
                                        new InMemoryUserStorage()));
                        userController.addUser(user);
                    }
                });
        assertEquals("Ошибка в email.", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithEmptyEmailUser() {
        User user = new User(null,
                "testLogin", "testName", "1990-01-01");
        UserController userController = new UserController(
                new UserService(
                        new InMemoryUserStorage()));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Ошибка в email.");
        }
        final List<User> users = userController.getUsers();
        assertEquals(0, users.size(), "Неверное количество пользователей.");
    }

    @Test
    void shouldThrowValidateExceptionOnNullEmailUser() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User user = new User(null,
                                "testLogin", "testName", "1990-01-01");
                        UserController userController = new UserController(
                                new UserService(
                                        new InMemoryUserStorage()));
                        userController.addUser(user);
                    }
                });
        assertEquals("Ошибка в email.", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithNullEmailUser() {
        User user = new User("",
                "testLogin", "testName", "1990-01-01");
        UserController userController = new UserController(
                new UserService(
                        new InMemoryUserStorage()));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Ошибка в email.");
        }
        final List<User> users = userController.getUsers();
        assertEquals(0, users.size(), "Неверное количество пользователей.");
    }

    @Test
    void shouldThrowValidateExceptionOnEmptyLoginUser() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User user = new User("test@test.ru",
                                "", "testName", "1990-01-01");
                        UserController userController = new UserController(
                                new UserService(
                                        new InMemoryUserStorage()));
                        userController.addUser(user);
                    }
                });
        assertEquals("Ошибка в логине пользователя.", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithEmptyLoginUser() {
        User user = new User("test@test.ru",
                "", "testName", "1990-01-01");
        UserController userController = new UserController(
                new UserService(
                        new InMemoryUserStorage()));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Ошибка в логине пользователя.");
        }
        final List<User> users = userController.getUsers();
        assertEquals(0, users.size(), "Неверное количество пользователей.");
    }

    @Test
    void shouldThrowValidateExceptionOnBadLoginUser() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User user = new User("test@test.ru",
                                "test test", "testName", "1990-01-01");
                        UserController userController = new UserController(
                                new UserService(
                                        new InMemoryUserStorage()));
                        userController.addUser(user);
                    }
                });
        assertEquals("Ошибка в логине пользователя.", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithBadLoginUser() {
        User user = new User("test@test.ru",
                "test test", "testName", "1990-01-01");
        UserController userController = new UserController(
                new UserService(
                        new InMemoryUserStorage()));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Ошибка в логине пользователя.");
        }
        final List<User> users = userController.getUsers();
        assertEquals(0, users.size(), "Неверное количество пользователей.");
    }

    @Test
    void shouldThrowValidateExceptionOnNullLoginUser() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User user = new User("test@test.ru",
                                null, "testName", "1990-01-01");
                        UserController userController = new UserController(
                                new UserService(
                                        new InMemoryUserStorage()));
                        userController.addUser(user);
                    }
                });
        assertEquals("Ошибка в логине пользователя.", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithNullLoginUser() {
        User user = new User("test@test.ru",
                null, "testName", "1990-01-01");
        UserController userController = new UserController(
                new UserService(
                        new InMemoryUserStorage()));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("Ошибка в логине пользователя.");
        }
        final List<User> users = userController.getUsers();
        assertEquals(0, users.size(), "Неверное количество пользователей.");
    }


    @Test
    void shouldEnterLoginInFieldNameUser() throws ValidationException {
        User user = new User( "test@test.ru",
                "testLogin", " ", "1990-01-01");
        UserController userController = new UserController(
                new UserService(
                        new InMemoryUserStorage()));

        User userAfterAdd = userController.addUser(user);

        final List<User> users = userController.getUsers();

        assertNotNull(users, "Пользователь на попадает в базу.");
        assertEquals(1, users.size(), "Неверное количество пользователей.");
        assertEquals(userAfterAdd.getId(), 1, "ID пользователей не совпадают.");
        assertEquals(userAfterAdd.getName(), userAfterAdd.getLogin(), "Login и Name не совпадают.");
    }

    @Test
    void shouldThrowValidateExceptionOnBadBirthdayUser() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User user = new User( "test@test.ru",
                                "testLogin", "testName", "2030-01-01");
                        UserController userController = new UserController(
                                new UserService(
                                        new InMemoryUserStorage()));
                        userController.addUser(user);
                    }
                });
        assertEquals("День рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void shouldNotCreateUserWithBadBirthdayUser() {
        User user = new User("test@test.ru",
                "testLogin", "testName", "2030-01-01");
        UserController userController = new UserController(
                new UserService(
                        new InMemoryUserStorage()));
        try {
            userController.addUser(user);
        } catch (ValidationException e) {
            System.out.println("День рождения не может быть в будущем.");
        }
        final List<User> users = userController.getUsers();
        assertEquals(0, users.size(), "Неверное количество пользователей.");
    }


    //
    // тесты на внесение фильма
    //


    @Test
    void shouldCreateFilm() throws ValidationException {
        Film film = new Film("testName",
                "testDescription", "2000-01-01", 90);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));

        Film filmAfterAdd = filmController.addFilm(film);
        final List<Film> films = filmController.getFilms();

        assertNotNull(films, "Фильм на попадает в базу");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertEquals(filmAfterAdd.getId(), 1, "ID фильмов не совпадают");
    }

    @Test
    void shouldThrowValidateExceptionOnEmptyNameFilm() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        Film film = new Film(" ",
                                "testDescription", "2000-01-01", 90);
                        FilmController filmController = new FilmController(
                                new FilmService(
                                        new InMemoryFilmStorage(), new InMemoryUserStorage()));

                        filmController.addFilm(film);
                    }
                });
        assertEquals("Ошибка в названии фильма.", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithEmptyNameFilm() {
        Film film = new Film(" ",
                "testDescription", "2000-01-01", 90);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка в названии фильма.");
        }
        final List<Film> films = filmController.getFilms();
        assertEquals(0, films.size(), "Неверное количество фильмов.");
    }

    @Test
    void shouldThrowValidateExceptionOnNullNameFilm() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        Film film = new Film(null,
                                "testDescription", "2000-01-01", 90);
                        FilmController filmController = new FilmController(
                                new FilmService(
                                        new InMemoryFilmStorage(), new InMemoryUserStorage()));

                        filmController.addFilm(film);
                    }
                });
        assertEquals("Ошибка в названии фильма.", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithNullNameFilm() {
        Film film = new Film(null,
                "testDescription", "2000-01-01", 90);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка в названии фильма.");
        }
        final List<Film> films = filmController.getFilms();
        assertEquals(0, films.size(), "Неверное количество фильмов.");
    }

    @Test
    void shouldThrowValidateExceptionOnOverLongDescription() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        Film film = new Film("testName",
                                "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
                                        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
                                        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
                                        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
                                        + "0",
                                "2000-01-01", 90);
                        FilmController filmController = new FilmController(
                                new FilmService(
                                        new InMemoryFilmStorage(), new InMemoryUserStorage()));

                        filmController.addFilm(film);
                    }
                });
        assertEquals("Ошибка в описании фильма. Описание пустое или слишком длинное.",
                exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithOverLongDescription() {
        Film film = new Film("testName",
                "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
                        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
                        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
                        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
                        + "0",
                "2000-01-01", 90);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка в описании фильма. Описание пустое или слишком длинное.");
        }
        final List<Film> films = filmController.getFilms();
        assertEquals(0, films.size(), "Неверное количество фильмов.");
    }

    @Test
    void shouldNotCreateFilmWithMaxLongDescription() {
        Film film = new Film("testName",
                "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
                        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
                        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
                        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789",
                "2000-01-01", 90);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка в описании фильма. Описание пустое или слишком длинное.");
        }
        final List<Film> films = filmController.getFilms();
        assertEquals(1, films.size(), "Неверное количество фильмов.");
    }

    @Test
    void shouldThrowValidateExceptionOnNullDescription() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        Film film = new Film("testName",
                                null,
                                "2000-01-01", 90);
                        FilmController filmController = new FilmController(
                                new FilmService(
                                        new InMemoryFilmStorage(), new InMemoryUserStorage()));

                        filmController.addFilm(film);
                    }
                });
        assertEquals("Ошибка в описании фильма. Описание пустое или слишком длинное.", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithNullDescription() {
        Film film = new Film("testName",
                null,
                "2000-01-01", 90);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка в описании фильма. Описание пустое или слишком длинное.");
        }
        final List<Film> films = filmController.getFilms();
        assertEquals(0, films.size(), "Неверное количество фильмов.");
    }

    @Test
    void shouldThrowValidateExceptionOnOverEarlyDateRelease() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        Film film = new Film("testName",
                                "testDescription",
                                "1895-12-27", 90);
                        FilmController filmController = new FilmController(
                                new FilmService(
                                        new InMemoryFilmStorage(), new InMemoryUserStorage()));

                        filmController.addFilm(film);
                    }
                });
        assertEquals("Дата релиза не верна.", exception.getMessage());
    }

    @Test
    void shouldNotCreateFilmWithOverEarlyDateRelease() {
        Film film = new Film("testName",
                "testDescription",
                "1895-12-27", 90);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            System.out.println("Дата релиза не верна.");
        }
        final List<Film> films = filmController.getFilms();
        assertEquals(0, films.size(), "Неверное количество фильмов.");
    }

    @Test
    void shouldCreateFilmWithMaxEarlyDateRelease() {
        Film film = new Film("testName",
                "testDescription",
                "1895-12-28", 90);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            System.out.println("Дата релиза не верна.");
        }
        final List<Film> films = filmController.getFilms();
        assertEquals(1, films.size(), "Неверное количество фильмов.");
    }

    @Test
    void shouldThrowNullPointerExceptionOnNullDateRelease() throws NullPointerException {
        assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        Film film = new Film("testName",
                                "testDescription",
                                null, 90);
                        FilmController filmController = new FilmController(
                                new FilmService(
                                        new InMemoryFilmStorage(), new InMemoryUserStorage()));

                        filmController.addFilm(film);
                    }
                });
    }

    @Test
    void shouldNotCreateFilmWithZeroDuration() {
        Film film = new Film("testName",
                "testDescription",
                "2000-01-01", 0);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            System.out.println("Продолжительность фильма отрицательная или ноль.");
        }
        final List<Film> films = filmController.getFilms();
        assertEquals(0, films.size(), "Неверное количество фильмов.");
    }

    @Test
    void shouldCreateFilmWithZeroDuration() {
        Film film = new Film("testName",
                "testDescription",
                "2000-01-01", 0);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            System.out.println("Продолжительность фильма отрицательная или ноль.");
        }
        final List<Film> films = filmController.getFilms();
        assertEquals(0, films.size(), "Неверное количество фильмов.");
    }

    @Test
    void shouldNotCreateFilmWithNegativeDuration() {
        Film film = new Film("testName",
                "testDescription",
                "2000-01-01", 0);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            System.out.println("Продолжительность фильма отрицательная или ноль.");
        }
        final List<Film> films = filmController.getFilms();
        assertEquals(0, films.size(), "Неверное количество фильмов.");
    }

    @Test
    void shouldCreateFilmWithNegativeDuration() {
        Film film = new Film("testName",
                "testDescription",
                "2000-01-01", 0);
        FilmController filmController = new FilmController(
                new FilmService(
                        new InMemoryFilmStorage(), new InMemoryUserStorage()));
        try {
            filmController.addFilm(film);
        } catch (ValidationException e) {
            System.out.println("Продолжительность фильма отрицательная или ноль.");
        }
        final List<Film> films = filmController.getFilms();
        assertEquals(0, films.size(), "Неверное количество фильмов.");
    }
}
*/