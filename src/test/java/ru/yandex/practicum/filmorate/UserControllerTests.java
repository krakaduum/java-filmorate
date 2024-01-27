package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserControllerTests {
    private UserController userController;
    private User user;

    @BeforeEach
    void beforeEach() {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        userController = new UserController(userService);
        user = new User();
    }

    @Test
    void shouldNotBeValidWhenEmptyLogin() {
        user.setId(0);
        user.setLogin("");
        user.setName("name");
        user.setEmail("email@mail");
        user.setBirthday(LocalDate.of(2020, 10, 20));

        Exception exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
        assertEquals("Валидация не пройдена", exception.getMessage());
    }

    @Test
    void shouldNotBeValidWhenLoginHasSpaces() {
        user.setId(0);
        user.setLogin("login login");
        user.setName("name");
        user.setEmail("email@mail");
        user.setBirthday(LocalDate.of(2020, 10, 20));

        Exception exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
        assertEquals("Валидация не пройдена", exception.getMessage());
    }

    @Test
    void nameShouldBeTheSameAsLogin() {
        user.setId(0);
        user.setLogin("login");
        user.setName("");
        user.setEmail("email@mail");
        user.setBirthday(LocalDate.of(2020, 10, 20));
        userController.addUser(user);

        assertEquals("login", user.getName());
    }

    @Test
    void shouldNotBeValidWhenWrongDate() {
        user.setId(0);
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@mail");
        user.setBirthday(LocalDate.of(2024, 10, 20));

        Exception exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
        assertEquals("Валидация не пройдена", exception.getMessage());
    }

    @Test
    void shouldNotBeValidWhenWrongEmail() {
        user.setId(0);
        user.setLogin("");
        user.setName("name");
        user.setEmail("email");
        user.setBirthday(LocalDate.of(2020, 10, 20));

        Exception exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
        assertEquals("Валидация не пройдена", exception.getMessage());
    }
}
