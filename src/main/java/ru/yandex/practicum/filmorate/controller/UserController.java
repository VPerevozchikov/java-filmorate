package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;

@RestController
public class UserController extends AbstractController<User> {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public ArrayList getUsers() {
        log.info("Количество пользователей в базе данных: {}", data.size());
        return new ArrayList<>(data.values());
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        if (user.getId() != null) {
            log.info("Запрос на обновление информации по существующему пользователю в методе POST не корректен.");
            return null;
        }
        log.info("Запрос на создание пользователя: {}", user);
        return createItem(user);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (user.getId() == null) {
            log.info("Запрос на создание нового пользователя в методе PUT не корректен.");
            return null;
        }
        log.info("Запрос на обновление данных пользователя: {}", user);
        return updateItem(user);
    }

    @Override
    void validate(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("Ошибка в email{}: ", user.getEmail());
            throw new ValidationException("Ошибка в email.");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Ошибка в логине пользователя: {}", user.getLogin());
            throw new ValidationException("Ошибка в логине пользователя.");
        }

        if (user.getName() == null || user.getName().isBlank() || user.getName().length() == 0) {
            log.info("Поле 'name' пустое. В поле 'name' вставлены данные поля 'login': {}.", user.getLogin());
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Введенный день рождения {} является будущим временем, это не корректно", user.getBirthday());
            throw new ValidationException("День рождения не может быть в будущем.");
        }
    }
}
