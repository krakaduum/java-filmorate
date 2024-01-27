package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public User addUser(User user);

    public void updateUser(User user);

    public void deleteUser(int id);

    public User getUser(int id);

    public Collection<User> getUsers();

    public Collection<User> getMutualFriends(int id, int otherId);

    public Collection<User> getUserFriends(int id);

    public void addFriend(int id, int friendId);

    public void removeFriend(int id, int friendId);
}
