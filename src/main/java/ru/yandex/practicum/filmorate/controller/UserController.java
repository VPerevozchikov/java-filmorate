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

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Количество пользователей в базе данных: {}", userService.getUsers().size());
        return userService.getUsers();
    }

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Запрос на создание пользователя: {}", user);
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Запрос на обновление данных пользователя: {}", user);
        return userService.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Запрос на добавление пользователя в друзья: {}", userService.getUserById(friendId));
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        log.info("Запрос на удаление пользователя из друзей: {}", userService.getUserById(friendId));
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getListOfFriends(@PathVariable("id") Long id) {
        log.info("Запрос на получение списка друзей пользователя: {}", userService.getUserById(id));
        return userService.getListOfFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getListOfMutualFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        log.info("Запрос на получение списка общих друзей пользователя: {}", userService.getUserById(id));
        return userService.getListOfMutualFriends(id, otherId);
    }

    @GetMapping("/users/{userId}")
    public User findUser(@PathVariable("userId") Long userId) {
        log.info("Запрос на получение пользователя по id: {}", userId);
        return userService.getUserById(userId);
    }
}