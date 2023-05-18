package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class User {
    private int id;

    @Email
    @NotNull
    @NotBlank
    private String email;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "Не может содержать пробел")
    private String login;

    private String name;

    @Past(message = "Дата рождения должна быть раньше текущей даты")
    private LocalDate birthday;

    @JsonIgnore
    private Map<Integer, User> friends = new HashMap<>();

    public void addFriend(User user) {
        friends.put(user.getId(), user);
        user.friends.put(this.id, this);
    }

    public void removeFriend(User user) {
        if (friends.containsKey(user.getId())) {
            friends.remove(user.getId());
            user.friends.remove(this.id);
        }
    }
}
