package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    void addUser(User user);

    void deleteUser(User user);

    Map<Long, User> getUsers();
}