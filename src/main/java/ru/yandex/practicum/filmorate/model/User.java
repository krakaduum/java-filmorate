package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    int id;

    @Email
    @NotNull
    @NotBlank
    String email;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "Не может содержать пробел")
    String login;

    String name;

    @Past(message = "Дата рождения должна быть раньше текущей даты")
    LocalDate birthday;

    @JsonIgnore
    Map<Integer, User> friends = new HashMap<>();

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
