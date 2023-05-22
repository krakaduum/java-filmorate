package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class InMemoryUserStorage implements UserStorage {
    Map<Integer, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
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
