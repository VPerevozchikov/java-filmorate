package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    boolean deleteUserById(Long id);

    User updateUser(User user);

    User getUserById(Long id);

    void addFriend(Long id1, Long id2);

    void deleteFriend(Long id1, Long id2);

    List<User> getUsers();

    List<User> getListOfFriends(Long id);

    List<User> getListOfMutualFriends(Long id1, Long id2);

}