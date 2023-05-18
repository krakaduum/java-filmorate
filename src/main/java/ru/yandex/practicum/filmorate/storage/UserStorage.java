package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUser(int id);

    public User getUser(int id);

    public Collection<User> getUsers();
}
