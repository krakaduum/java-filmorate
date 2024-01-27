package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;

	private User user;
	private Film film;

	@BeforeEach
	public void beforeEach() {
		user = new User();
		user.setId(1);
		user.setLogin("login");
		user.setEmail("mail@mail.ru");
		user.setName("name");
		user.setBirthday(LocalDate.of(2000, 01, 01));

		film = new Film();
		film.setId(1);
		film.setName("name");
		film.setDuration(90);
		film.setReleaseDate(LocalDate.of(2000, 01, 01));
		film.setMpa(new Mpa(1, "G"));
		film.setDescription("description");
	}

	@Test
	public void testAddAndFindUser() {
		userStorage.addUser(user);
		Optional<User> userOptional = Optional.of(userStorage.getUser(user.getId()));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(u ->
						assertThat(u).hasFieldOrPropertyWithValue("id", user.getId())
				);
		userStorage.deleteUser(user.getId());
	}

	@Test
	public void testGetAllUsers() {
		userStorage.addUser(user);
		Collection<User> users = userStorage.getUsers();

		assertThat(users.size()).isEqualTo(1);
		userStorage.deleteUser(user.getId());
	}

	@Test
	public void testUpdateUser() {
		userStorage.addUser(user);
		User updatedUser = new User();
		updatedUser.setId(user.getId());
		updatedUser.setLogin("login");
		updatedUser.setEmail("mail@mail.ru");
		updatedUser.setName("newname");
		updatedUser.setBirthday(LocalDate.of(2000, 01, 01));

		userStorage.updateUser(updatedUser);

		Optional<User> userOptional = Optional.of(userStorage.getUser(user.getId()));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(u ->
						assertThat(u).hasFieldOrPropertyWithValue("name", "newname")
				);
		userStorage.deleteUser(user.getId());
	}

	@Test
	public void testAddFriend() {
		userStorage.addUser(user);
		User friend = new User();
		friend.setId(2);
		friend.setLogin("friend");
		friend.setEmail("friend@mail.ru");
		friend.setName("friend");
		friend.setBirthday(LocalDate.of(2000, 01, 01));

		userStorage.addUser(friend);
		userStorage.addFriend(user.getId(), friend.getId());

		Collection<User> users = userStorage.getUserFriends(user.getId());

		assertThat(users.size()).isEqualTo(1);

		userStorage.removeFriend(user.getId(), friend.getId());
		userStorage.deleteUser(user.getId());
		userStorage.deleteUser(friend.getId());
	}

	@Test
	public void testRemoveFriend() {
		userStorage.addUser(user);
		User friend = new User();
		friend.setId(2);
		friend.setLogin("friend");
		friend.setEmail("friend@mail.ru");
		friend.setName("friend");
		friend.setBirthday(LocalDate.of(2000, 01, 01));

		userStorage.addUser(friend);
		userStorage.addFriend(user.getId(), friend.getId());

		userStorage.removeFriend(user.getId(), friend.getId());
		Collection<User> users = userStorage.getUserFriends(user.getId());

		assertThat(users.size()).isEqualTo(0);

		userStorage.deleteUser(user.getId());
		userStorage.deleteUser(friend.getId());
	}

	@Test
	public void testDeleteUser() {
		userStorage.addUser(user);

		userStorage.deleteUser(user.getId());

		Exception exception = assertThrows(NoSuchElementException.class, () -> {
			userStorage.getUser(user.getId());
		});
		assertEquals("Пользователь с id = " + user.getId() + " не найден", exception.getMessage());
	}

	@Test
	public void testAddAndFindFilm() {
		filmStorage.addFilm(film);
		Optional<Film> filmOptional = Optional.of(filmStorage.getFilm(film.getId()));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(f ->
						assertThat(f).hasFieldOrPropertyWithValue("id", film.getId())
				);
		filmStorage.deleteFilm(film.getId());
	}

	@Test
	public void testGetAllFilms() {
		filmStorage.addFilm(film);
		Collection<Film> films = filmStorage.getFilms();

		assertThat(films.size()).isEqualTo(1);
		filmStorage.deleteFilm(film.getId());
	}

	@Test
	public void testUpdateFilm() {
		filmStorage.addFilm(film);
		Film updatedFilm = new Film();
		updatedFilm.setId(film.getId());
		updatedFilm.setName("newname");
		updatedFilm.setDuration(90);
		updatedFilm.setReleaseDate(LocalDate.of(2000, 01, 01));
		updatedFilm.setMpa(new Mpa(1, "G"));
		updatedFilm.setDescription("description");

		filmStorage.updateFilm(updatedFilm);

		Optional<Film> filmOptional = Optional.of(filmStorage.getFilm(film.getId()));

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(f ->
						assertThat(f).hasFieldOrPropertyWithValue("name", "newname")
				);
		filmStorage.deleteFilm(film.getId());
	}
}
