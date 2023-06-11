package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.NoSuchElementException;

@Qualifier("userDbStorage")
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into users (email, login, name, birthday) " +
                "values (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        long idKey = keyHolder.getKey().longValue();
        user.setId((int) idKey);
        return user;
    }

    @Override
    public void updateUser(User user) {
        String sqlQuery = "update users set email=?, login=?, name=?, birthday=? where id=?";

        int updatedUsers = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        if (updatedUsers == 0) {
            throw new NoSuchElementException("Пользователь с id = " + user.getId() + " не найден");
        }

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public void deleteUser(int id) {
        String sqlQuery = "delete from users where id=?";

        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public User getUser(int id) {
        String sqlQuery = "select id, email, login, name, birthday from users where id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sqlQuery,
                    (ResultSet rs, int rowNum) -> createUserObject(rs), id);
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Пользователь с id = " + id + " не найден");
        }
    }

    @Override
    public Collection<User> getUsers() {
        String sqlQuery = "select id, email, login, name, birthday from users";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> createUserObject(rs));
    }

    @Override
    public Collection<User> getMutualFriends(int id, int otherId) {
        String sqlQuery = "select id, email, login, name, birthday from users " +
                "where id " +
                "in(select friend2_id " +
                "from friends " +
                "where friend1_id = ?) " +
                "and id " +
                "in(select friend2_id " +
                "from friends " +
                "where friend1_id = ?)";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> createUserObject(rs), id, otherId);
    }

    private User createUserObject(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    public Collection<User> getUserFriends(int id) {
        String sqlQuery = "select u.id, u.email, u.login, u.name, u.birthday from friends f " +
                "left join users u on f.friend2_id=u.id " +
                "where f.friend1_id=?";
        try {
            return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> createUserObject(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Друзья пользователя с id = " + id + " не найдены");
        }
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sqlQuery = "insert into friends (friend1_id, friend2_id)" +
                "values (?,?)";

        try {
            jdbcTemplate.update(sqlQuery, id, friendId);
        }
        catch (Exception exception) {
            throw new NoSuchElementException("Пользователи не найдены");
        }
    }

    @Override
    public void removeFriend(int id, int friendId) {
        String sqlQuery = "delete from friends where friend1_id=? and friend2_id=?";

        jdbcTemplate.update(sqlQuery, id, friendId);
    }
}
