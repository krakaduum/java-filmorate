package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (users.containsKey(user.getEmail())) {
            String userAlreadyExists = "Пользователь с такой почтой уже существует";
            log.error(userAlreadyExists);
            throw new ValidationException(userAlreadyExists);
        }
        if (users.containsKey(user.getId())) {
            String userAlreadyExists = "Пользователь с таким id уже существует";
            log.error(userAlreadyExists);
            throw new ValidationException(userAlreadyExists);
        }

        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            log.error(violation.getMessage());
            throw new ValidationException("Валидация не пройдена");
        }

        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getEmail())) {
            String userDoesNotExist = "Пользователь с такой почтой не существует";
            log.error(userDoesNotExist);
            throw new ValidationException(userDoesNotExist);
        }
        if (users.containsKey(user.getId())) {
            String userDoesNotExist = "Пользователь с таким id не существует";
            log.error(userDoesNotExist);
            throw new ValidationException(userDoesNotExist);
        }

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            log.error(violation.getMessage());
            throw new ValidationException("Валидация не пройдена");
        }

        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }


        users.put(user.getId(), user);
        return user;
    }
}
