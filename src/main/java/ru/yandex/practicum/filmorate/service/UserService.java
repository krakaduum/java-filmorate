package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        if (user == null || friend == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        user.addFriend(friend);
    }

    public void removeFriend(int id, int friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        if (user == null || friend == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        user.removeFriend(friend);
    }

    public Collection<User> getUserFriends(int id) {
        User user = userStorage.getUser(id);
        if (user == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        return user.getFriends().values();
    }

    public Collection<User> getMutualFriends(int id, int otherId) {
        User user = userStorage.getUser(id);
        User other = userStorage.getUser(otherId);
        if (user == null || other == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        List<User> mutualFriends = new ArrayList<>();
        for (User friend : user.getFriends().values()) {
            if (other.getFriends().containsKey(friend.getId())) {
                mutualFriends.add(friend);
            }
        }
        return mutualFriends;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public void addUser(User user) {
        userStorage.addUser(user);
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);
    }
}
