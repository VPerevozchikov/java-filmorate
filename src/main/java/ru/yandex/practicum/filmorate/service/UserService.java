package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;
    private Long userId = 0L;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) throws ValidationException {
        validate(user);
        user.setId(++userId);
        userStorage.addUser(user);
        return user;
    }

    public User updateUser(User user) throws ValidationException {
        validate(user);
        if (userStorage.getUsers().containsKey(user.getId())) {
            userStorage.addUser(user);
        } else {
            throw new UserNotFoundException(String.format(
                    "Пользователь c id %s не найден", user.getId()));
        }
        return user;
    }

    public void deleteUser(User user) {
        if (userStorage.getUsers().containsKey(user.getId())) {
            userStorage.deleteUser(user);
        } else {
            throw new UserNotFoundException(String.format(
                    "Пользователь c id %s не найден", user.getId()));
        }
    }

    public List<User> getUsers() {
        return new ArrayList<>(userStorage.getUsers().values());
    }

    public User getUserById(Long id) {
        if (userStorage.getUsers().containsKey(id)) {
            return userStorage.getUsers().get(id);
        } else {
            throw new UserNotFoundException(String.format(
                    "Пользователь c id %s не найден", id));
        }
    }

    public void addFriend(Long id1, Long id2) {
        if (userStorage.getUsers().containsKey(id1)
                && userStorage.getUsers().containsKey(id2)) {
            getUserById(id1).listOfFriends.add(id2);
            getUserById(id2).listOfFriends.add(id1);
        } else {
            throw new UserNotFoundException(String.format(
                    "Пользователь/и c id %s / %s не найдены", id1, id2));
        }
    }

    public void deleteFriend(Long id1, Long id2) {
        if (userStorage.getUsers().containsKey(id1)
                && userStorage.getUsers().containsKey(id2)) {
            getUserById(id1).listOfFriends.remove(id2);
            getUserById(id2).listOfFriends.remove(id1);
        } else {
            throw new UserNotFoundException(String.format(
                    "Пользователь/и c id %s / %s не найдены", id1, id2));
        }
    }

    public List<User> getListOfMutualFriends(Long id1, Long id2) {
        if (userStorage.getUsers().containsKey(id1)
                && userStorage.getUsers().containsKey(id2)) {
            Set<Long> mutualIdOfFriends = new HashSet<>();
            List<User> mutualFriends = new ArrayList<>();
            Set<Long> u1IdFriends = getUserById(id1).listOfFriends;
            Set<Long> u2IdFriends = getUserById(id2).listOfFriends;

            for (Long id : u1IdFriends) {
                if (u2IdFriends.contains(id)) {
                    mutualIdOfFriends.add(id);
                }
            }
            for (Long id : mutualIdOfFriends) {
                mutualFriends.add(userStorage.getUsers().get(id));
            }
            return mutualFriends;
        } else {
            throw new UserNotFoundException(String.format(
                    "Пользователь/и c id %s / %s не найдены", id1, id2));
        }
    }

    public List<User> getListOfFriends(Long id) {
        if (userStorage.getUsers().containsKey(id)) {
            List<User> friends = new ArrayList<>();

            for (Long idFriend : getUserById(id).listOfFriends) {
                friends.add(userStorage.getUsers().get(idFriend));
            }
            return friends;
        } else {
            throw new UserNotFoundException(String.format(
                    "Пользователь c id %s не найден", id));
        }
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