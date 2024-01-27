package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.IsAfter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Film {
    int id;

    @NotNull
    @NotBlank
    String name;

    @Size(max = 200)
    String description;

    @IsAfter(current = "1895-12-27", message = "Дата должна быть больше 1895-12-27")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate releaseDate;

    @Positive
    int duration;

    Mpa mpa;

    Set<Genre> genres;

    Set<Integer> likes = new HashSet<>();

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void removeLike(int userId) {
        if (likes.contains(userId)) {
            likes.remove(userId);
        }
    }
}
