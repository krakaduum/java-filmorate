package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        userStorage.addFriend(id, friendId);

        log.info("Пользователь " + id + " добавил в друзья пользователя " + friendId);
    }

    public void removeFriend(int id, int friendId) {
        userStorage.removeFriend(id, friendId);

        log.info("Пользователь " + id + " удалил из друзей пользователя " + friendId);
    }

    public Collection<User> getUserFriends(int id) {
        return userStorage.getUserFriends(id);
    }

    public Collection<User> getMutualFriends(int id, int otherId) {
        return userStorage.getMutualFriends(id, otherId);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public void addUser(User user) {
        userStorage.addUser(user);

        log.info("Добавлен пользователь с идентификатором " + user.getId());
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);

        log.info("Обновлен пользователь с идентификатором " + user.getId());
    }
}
