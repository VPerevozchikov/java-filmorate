package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FilmorateApplicationTests {


	@Test
	void contextLoads() {
	}

	@Test
	void shouldCreateUser() throws ValidationException {
		User user = new User(null, "test@test.ru",
				"testLogin", "testName", "1990-01-01");

		UserController userController = new UserController();

		User userAfterAdd = userController.addUser(user);
		final List<User> users = userController.getUsers();

		assertNotNull(users, "Пользователь на попадает в базу.");
		assertEquals(1, users.size(), "Неверное количество пользователей.");
		assertEquals(userAfterAdd.getId(), 1, "ID пользователей не совпадают.");
	}

	@Test
	void shouldThrowValidateExceptionOnBadEmailUser() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						User user = new User(null, "test.ru",
								"testLogin", "testName", "1990-01-01");
						UserController userController = new UserController();
						userController.addUser(user);
					}
				});
		assertEquals("Ошибка в email.", exception.getMessage());
	}

	@Test
	void shouldNotCreateUserWithBadEmailUser() throws ValidationException {
		User user = new User(null, "test.ru",
				"testLogin", "testName", "1990-01-01");
		UserController userController = new UserController();
		try {
			userController.addUser(user);
		} catch (ValidationException e) {
			System.out.println("Ошибка в email.");
		}
		final List<User> users = userController.getUsers();
		assertEquals(0, users.size(), "Неверное количество пользователей.");
	}

	@Test
	void shouldThrowValidateExceptionOnEmptyEmailUser() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						User user = new User(null, "",
								"testLogin", "testName", "1990-01-01");
						UserController userController = new UserController();
						userController.addUser(user);
					}
				});
		assertEquals("Ошибка в email.", exception.getMessage());
	}

	@Test
	void shouldNotCreateUserWithEmptyEmailUser() throws ValidationException {
		User user = new User(null, null,
				"testLogin", "testName", "1990-01-01");
		UserController userController = new UserController();
		try {
			userController.addUser(user);
		} catch (ValidationException e) {
			System.out.println("Ошибка в email.");
		}
		final List<User> users = userController.getUsers();
		assertEquals(0, users.size(), "Неверное количество пользователей.");
	}

	@Test
	void shouldThrowValidateExceptionOnNullEmailUser() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						User user = new User(null, null,
								"testLogin", "testName", "1990-01-01");
						UserController userController = new UserController();
						userController.addUser(user);
					}
				});
		assertEquals("Ошибка в email.", exception.getMessage());
	}

	@Test
	void shouldNotCreateUserWithNullEmailUser() throws ValidationException {
		User user = new User(null, "",
				"testLogin", "testName", "1990-01-01");
		UserController userController = new UserController();
		try {
			userController.addUser(user);
		} catch (ValidationException e) {
			System.out.println("Ошибка в email.");
		}
		final List<User> users = userController.getUsers();
		assertEquals(0, users.size(), "Неверное количество пользователей.");
	}

	@Test
	void shouldThrowValidateExceptionOnEmptyLoginUser() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						User user = new User(null, "test@test.ru",
								"", "testName", "1990-01-01");
						UserController userController = new UserController();
						userController.addUser(user);
					}
				});
		assertEquals("Ошибка в логине пользователя.", exception.getMessage());
	}

	@Test
	void shouldNotCreateUserWithEmptyLoginUser() throws ValidationException {
		User user = new User(null, "test@test.ru",
				"", "testName", "1990-01-01");
		UserController userController = new UserController();
		try {
			userController.addUser(user);
		} catch (ValidationException e) {
			System.out.println("Ошибка в логине пользователя.");
		}
		final List<User> users = userController.getUsers();
		assertEquals(0, users.size(), "Неверное количество пользователей.");
	}

	@Test
	void shouldThrowValidateExceptionOnBadLoginUser() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						User user = new User(null, "test@test.ru",
								"test test", "testName", "1990-01-01");
						UserController userController = new UserController();
						userController.addUser(user);
					}
				});
		assertEquals("Ошибка в логине пользователя.", exception.getMessage());
	}

	@Test
	void shouldNotCreateUserWithBadLoginUser() throws ValidationException {
		User user = new User(null, "test@test.ru",
				"test test", "testName", "1990-01-01");
		UserController userController = new UserController();
		try {
			userController.addUser(user);
		} catch (ValidationException e) {
			System.out.println("Ошибка в логине пользователя.");
		}
		final List<User> users = userController.getUsers();
		assertEquals(0, users.size(), "Неверное количество пользователей.");
	}

	@Test
	void shouldThrowValidateExceptionOnNullLoginUser() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						User user = new User(null, "test@test.ru",
								null, "testName", "1990-01-01");
						UserController userController = new UserController();
						userController.addUser(user);
					}
				});
		assertEquals("Ошибка в логине пользователя.", exception.getMessage());
	}

	@Test
	void shouldNotCreateUserWithNullLoginUser() throws ValidationException {
		User user = new User(null, "test@test.ru",
				null, "testName", "1990-01-01");
		UserController userController = new UserController();
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
		User user = new User(null, "test@test.ru",
				"testLogin", " ", "1990-01-01");
		UserController userController = new UserController();

		User userAfterAdd = userController.addUser(user);

		final List<User> users = userController.getUsers();

		assertNotNull(users, "Пользователь на попадает в базу.");
		assertEquals(1, users.size(), "Неверное количество пользователей.");
		assertEquals(userAfterAdd.getId(), 1, "ID пользователей не совпадают.");
		assertEquals(userAfterAdd.getName(), userAfterAdd.getLogin(), "Login и Name не совпадают.");
	}

	@Test
	void shouldThrowValidateExceptionOnBadBirthdayUser() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						User user = new User(null, "test@test.ru",
								"testLogin", "testName", "2030-01-01");
						UserController userController = new UserController();
						userController.addUser(user);
					}
				});
		assertEquals("День рождения не может быть в будущем.", exception.getMessage());
	}

	@Test
	void shouldNotCreateUserWithBadBirthdayUser() throws ValidationException {
		User user = new User(null, "test@test.ru",
				"testLogin", "testName", "2030-01-01");
		UserController userController = new UserController();
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
		Film film = new Film(null, "testName",
				"testDescription", "2000-01-01", 90);
		FilmController filmController = new FilmController();

		Film filmAfterAdd = filmController.addFilm(film);
		final List<Film> films = filmController.getFilms();

		assertNotNull(films, "Фильм на попадает в базу");
		assertEquals(1, films.size(), "Неверное количество фильмов.");
		assertEquals(filmAfterAdd.getId(), 1, "ID фильмов не совпадают");
	}

	@Test
	void shouldThrowValidateExceptionOnEmptyNameFilm() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						Film film = new Film(null, " ",
								"testDescription", "2000-01-01", 90);
						FilmController filmController = new FilmController();

						filmController.addFilm(film);
					}
				});
		assertEquals("Ошибка в названии фильма.", exception.getMessage());
	}

	@Test
	void shouldNotCreateFilmWithEmptyNameFilm() throws ValidationException {
		Film film = new Film(null, " ",
				"testDescription", "2000-01-01", 90);
		FilmController filmController = new FilmController();
		try {
			filmController.addFilm(film);
		} catch (ValidationException e) {
			System.out.println("Ошибка в названии фильма.");
		}
		final List<Film> films = filmController.getFilms();
		assertEquals(0, films.size(), "Неверное количество фильмов.");
	}

	@Test
	void shouldThrowValidateExceptionOnNullNameFilm() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						Film film = new Film(null, null,
								"testDescription", "2000-01-01", 90);
						FilmController filmController = new FilmController();

						filmController.addFilm(film);
					}
				});
		assertEquals("Ошибка в названии фильма.", exception.getMessage());
	}

	@Test
	void shouldNotCreateFilmWithNullNameFilm() throws ValidationException {
		Film film = new Film(null, null,
				"testDescription", "2000-01-01", 90);
		FilmController filmController = new FilmController();
		try {
			filmController.addFilm(film);
		} catch (ValidationException e) {
			System.out.println("Ошибка в названии фильма.");
		}
		final List<Film> films = filmController.getFilms();
		assertEquals(0, films.size(), "Неверное количество фильмов.");
	}

	@Test
	void shouldThrowValidateExceptionOnOverLongDescription() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						Film film = new Film(null, "testName",
								"0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
								        + "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
										+ "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
										+ "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
								        + "0",
								"2000-01-01", 90);
						FilmController filmController = new FilmController();

						filmController.addFilm(film);
					}
				});
		assertEquals("Ошибка в описании фильма. Описание пустое или слишком длинное.", exception.getMessage());
	}

	@Test
	void shouldNotCreateFilmWithOverLongDescription() throws ValidationException {
		Film film = new Film(null, "testName",
				"0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
						+ "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
						+ "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
						+ "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
						+ "0",
				"2000-01-01", 90);
		FilmController filmController = new FilmController();
		try {
			filmController.addFilm(film);
		} catch (ValidationException e) {
			System.out.println("Ошибка в описании фильма. Описание пустое или слишком длинное.");
		}
		final List<Film> films = filmController.getFilms();
		assertEquals(0, films.size(), "Неверное количество фильмов.");
	}

	@Test
	void shouldNotCreateFilmWithMaxLongDescription() throws ValidationException {
		Film film = new Film(null, "testName",
				"0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
						+ "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
						+ "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789"
						+ "0123456789" + "0123456789" + "0123456789" + "0123456789" + "0123456789",
				"2000-01-01", 90);
		FilmController filmController = new FilmController();
		try {
			filmController.addFilm(film);
		} catch (ValidationException e) {
			System.out.println("Ошибка в описании фильма. Описание пустое или слишком длинное.");
		}
		final List<Film> films = filmController.getFilms();
		assertEquals(1, films.size(), "Неверное количество фильмов.");
	}

	@Test
	void shouldThrowValidateExceptionOnNullDescription() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						Film film = new Film(null, "testName",
								null,
								"2000-01-01", 90);
						FilmController filmController = new FilmController();

						filmController.addFilm(film);
					}
				});
		assertEquals("Ошибка в описании фильма. Описание пустое или слишком длинное.", exception.getMessage());
	}

	@Test
	void shouldNotCreateFilmWithNullDescription() throws ValidationException {
		Film film = new Film(null, "testName",
				null,
				"2000-01-01", 90);
		FilmController filmController = new FilmController();
		try {
			filmController.addFilm(film);
		} catch (ValidationException e) {
			System.out.println("Ошибка в описании фильма. Описание пустое или слишком длинное.");
		}
		final List<Film> films = filmController.getFilms();
		assertEquals(0, films.size(), "Неверное количество фильмов.");
	}

	@Test
	void shouldThrowValidateExceptionOnOverEarlyDateRelease() throws ValidationException {
		final ValidationException exception = assertThrows(
				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						Film film = new Film(null, "testName",
								"testDescription",
								"1895-12-27", 90);
						FilmController filmController = new FilmController();

						filmController.addFilm(film);
					}
				});
		assertEquals("Дата релиза не верна.", exception.getMessage());
	}

	@Test
	void shouldNotCreateFilmWithOverEarlyDateRelease() throws ValidationException {
		Film film = new Film(null, "testName",
				"testDescription",
				"1895-12-27", 90);
		FilmController filmController = new FilmController();
		try {
			filmController.addFilm(film);
		} catch (ValidationException e) {
			System.out.println("Дата релиза не верна.");
		}
		final List<Film> films = filmController.getFilms();
		assertEquals(0, films.size(), "Неверное количество фильмов.");
	}

	@Test
	void shouldCreateFilmWithMaxEarlyDateRelease() throws ValidationException {
		Film film = new Film(null, "testName",
				"testDescription",
				"1895-12-28", 90);
		FilmController filmController = new FilmController();
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
		final NullPointerException exception = assertThrows(
				NullPointerException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						Film film = new Film(null, "testName",
								"testDescription",
								null, 90);
						FilmController filmController = new FilmController();

						filmController.addFilm(film);
					}
				});
	}

	@Test
	void shouldNotCreateFilmWithZeroDuration() throws ValidationException {
		Film film = new Film(null, "testName",
				"testDescription",
				"2000-01-01", 0);
		FilmController filmController = new FilmController();
		try {
			filmController.addFilm(film);
		} catch (ValidationException e) {
			System.out.println("Продолжительность фильма отрицательная или ноль.");
		}
		final List<Film> films = filmController.getFilms();
		assertEquals(0, films.size(), "Неверное количество фильмов.");
	}

	@Test
	void shouldCreateFilmWithZeroDuration() throws ValidationException {
		Film film = new Film(null, "testName",
				"testDescription",
				"2000-01-01", 0);
		FilmController filmController = new FilmController();
		try {
			filmController.addFilm(film);
		} catch (ValidationException e) {
			System.out.println("Продолжительность фильма отрицательная или ноль.");
		}
		final List<Film> films = filmController.getFilms();
		assertEquals(0, films.size(), "Неверное количество фильмов.");
	}

	@Test
	void shouldNotCreateFilmWithNegativeDuration() throws ValidationException {
		Film film = new Film(null, "testName",
				"testDescription",
				"2000-01-01", 0);
		FilmController filmController = new FilmController();
		try {
			filmController.addFilm(film);
		} catch (ValidationException e) {
			System.out.println("Продолжительность фильма отрицательная или ноль.");
		}
		final List<Film> films = filmController.getFilms();
		assertEquals(0, films.size(), "Неверное количество фильмов.");
	}

	@Test
	void shouldCreateFilmWithNegativeDuration() throws ValidationException {
		Film film = new Film(null, "testName",
				"testDescription",
				"2000-01-01", 0);
		FilmController filmController = new FilmController();
		try {
			filmController.addFilm(film);
		} catch (ValidationException e) {
			System.out.println("Продолжительность фильма отрицательная или ноль.");
		}
		final List<Film> films = filmController.getFilms();
		assertEquals(0, films.size(), "Неверное количество фильмов.");
	}


}
