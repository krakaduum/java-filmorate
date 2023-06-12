package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Qualifier("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    Map<Integer, User> users = new HashMap<>();

    public void addFriend(int id, int friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        if (user == null || friend == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        user.addFriend(friend);
    }

    public void removeFriend(int id, int friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        if (user == null || friend == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        user.removeFriend(friend);
    }

    public Collection<User> getUserFriends(int id) {
        User user = getUser(id);
        if (user == null) {
            throw new NoSuchElementException("Пользователь не найден");
        }
        return user.getFriends().values();
    }

    public Collection<User> getMutualFriends(int id, int otherId) {
        User user = getUser(id);
        User other = getUser(otherId);
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

    @Override
    public User addUser(User user) {
        if (users.containsKey(user.getEmail())) {
            throw new RuntimeException("Пользователь с почтой " + user.getEmail() + " уже существует");
        }
        if (users.containsKey(user.getId())) {
            throw new RuntimeException("Пользователь с id = " + user.getId() + " уже существует");
        }

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NoSuchElementException("Пользователя с id = " + user.getId() + " не существует");
        }

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
    }

    @Override
    public void deleteUser(int id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("Пользователя с id = " + id + " не существует");
        }

        users.remove(id);
    }

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("Пользователя с id = " + id + " не существует");
        }

        return users.get(id);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }
}
