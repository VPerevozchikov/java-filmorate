package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Запрос на создание пользователя: {}", user);
        return userService.addUser(user);
    }

    @DeleteMapping("/users/{userId}")
    public boolean deleteUser(@PathVariable("userId") Long userId) {
        log.info("Запрос на удаление пользователя по id: {}", userId);
        return userService.deleteUserById(userId);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Запрос на обновление данных пользователя: {}", user);
        return userService.updateUser(user);
    }

    @GetMapping("/users/{userId}")
    public User findUser(@PathVariable("userId") Long userId) {
        log.info("Запрос на получение пользователя по id: {}", userId);
        return userService.getUserById(userId);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Запрос на добавление пользователя c id в друзья: {}", friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Запрос на удаления пользователя с id из друзей: {}", friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Запрос на получение всех пользователей");
        return userService.getUsers();
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getListOfFriends(@PathVariable("id") Long id) {
        log.info("Запрос на получение списка друзей пользователя по id: {}", id);
        return userService.getListOfFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getListOfMutualFriends(@PathVariable("id") Long id1, @PathVariable("otherId") Long id2) {
        log.info("Запрос на получение списка общих друзей пользователя c id: {}", id1);
        return userService.getListOfMutualFriends(id1, id2);
    }
}