package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    Map<Integer, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        if (users.containsKey(user.getEmail())) {
            String userAlreadyExists = "Пользователь с такой почтой уже существует";
            log.error(userAlreadyExists);
            throw new RuntimeException(userAlreadyExists);
        }
        if (users.containsKey(user.getId())) {
            String userAlreadyExists = "Пользователь с таким id уже существует";
            log.error(userAlreadyExists);
            throw new RuntimeException(userAlreadyExists);
        }

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            String userDoesNotExist = "Пользователь с таким id не существует";
            log.error(userDoesNotExist);
            throw new NoSuchElementException(userDoesNotExist);
        }

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
    }

    @Override
    public void deleteUser(int id) {
        if (!users.containsKey(id)) {
            String userDoesNotExist = "Пользователь с таким id не существует";
            log.error(userDoesNotExist);
            throw new NoSuchElementException(userDoesNotExist);
        }

        users.remove(id);
    }

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id)) {
            String userDoesNotExist = "Пользователь с таким id не существует";
            log.error(userDoesNotExist);
            throw new NoSuchElementException(userDoesNotExist);
        }

        return users.get(id);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }
}
