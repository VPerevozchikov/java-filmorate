package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) throws ValidationException {
        validate(user);
        return userStorage.addUser(user);
    }

    public boolean deleteUserById(Long id) {
        return userStorage.deleteUserById(id);
    }

    public User updateUser(User user) throws ValidationException {
        validate(user);
        return userStorage.updateUser(user);
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(Long id1, Long id2) {
        userStorage.addFriend(id1, id2);
    }

    public void deleteFriend(Long id1, Long id2) {
        userStorage.deleteFriend(id1, id2);
    }

    public List<User> getUsers() {
        return new ArrayList<>(userStorage.getUsers());
    }

    public List<User> getListOfFriends(Long id) {
        return userStorage.getListOfFriends(id);
    }

    public List<User> getListOfMutualFriends(Long id1, Long id2) {
        return userStorage.getListOfMutualFriends(id1, id2);
    }
    public void validate(User user) throws ValidationException {
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