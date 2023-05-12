package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

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
}
