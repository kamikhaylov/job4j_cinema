package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.job4j.cinema.config.DataSourceConfigH2;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Тестрованиие работы с БД пользователей
 */
class CinemaUserRepositoryTest {
    private static final String EMAIL = "test@test.ru";
    private static final String PHONE = "123";

    private static DataSource dataSource;

    @BeforeEach
    public void before() {
        dataSource = new DataSourceConfigH2().loadDataSource();
    }

    @AfterEach
    public void after() throws SQLException {
        try (PreparedStatement st = dataSource.getConnection().prepareStatement(
                "DELETE FROM users;"
                        + "ALTER TABLE users ALTER COLUMN id RESTART WITH 1;")) {
            st.execute();
        }
    }

    @Test()
    public void whenCreateUser() {
        CinemaUserRepository store = new CinemaUserRepository(dataSource);
        User user1 = new User(0, "user1", "test1@test.ru", "123");
        User user2 = new User(0, "user2", "test2@test.ru", "321");

        Optional<User> addResult1 = store.add(user1);
        Optional<User> addResult2 = store.add(user2);
        List<User> users = store.findAll();

        Assertions.assertNotNull(addResult1);
        Assertions.assertNotNull(addResult2);
        Assertions.assertEquals(users.size(), 2);
        Assertions.assertEquals(addResult1.get().getEmail(), user1.getEmail());
        Assertions.assertEquals(addResult1.get().getUserName(), user1.getUserName());
        Assertions.assertEquals(addResult1.get().getPhone(), user1.getPhone());
        Assertions.assertEquals(addResult2.get().getEmail(), user2.getEmail());
        Assertions.assertEquals(addResult2.get().getUserName(), user2.getUserName());
        Assertions.assertEquals(addResult2.get().getPhone(), user2.getPhone());
    }

    @Test()
    public void whenCreateUserThenFail() {
        CinemaUserRepository store = new CinemaUserRepository(dataSource);
        User user1 = new User(0, "user1", EMAIL, PHONE);
        User user2 = new User(0, "user2", EMAIL, PHONE);

        Optional<User> addResult1 = store.add(user1);
        Optional<User> addResult2 = store.add(user2);
        List<User> users = store.findAll();

        Assertions.assertNotNull(addResult1);
        Assertions.assertEquals(addResult2, Optional.empty());
        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(addResult1.get().getEmail(), EMAIL);
        Assertions.assertEquals(addResult1.get().getUserName(), user1.getUserName());
        Assertions.assertEquals(addResult1.get().getPhone(), PHONE);
    }

    @Test()
    public void whenLogin() {
        CinemaUserRepository store = new CinemaUserRepository(dataSource);
        User user1 = new User(0, "user1", "test1@test.ru", "111");
        User user2 = new User(0, "user2", EMAIL, PHONE);

        store.add(user1);
        store.add(user2);

        Optional<User> result = store.findUserByPhone(PHONE);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get().getEmail(), EMAIL);
        Assertions.assertEquals(result.get().getUserName(), user2.getUserName());
        Assertions.assertEquals(result.get().getPhone(), PHONE);
    }

    private static Stream<Arguments> loginProvider() {
        return Stream.of(
                Arguments.of(EMAIL, "321"),
                Arguments.of(EMAIL, null),
                Arguments.of(null, PHONE),
                Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("loginProvider")
    public void whenLoginFail(String email, String phone) {
        CinemaUserRepository store = new CinemaUserRepository(dataSource);
        User user = new User(0, "user", email, PHONE);
        store.add(user);

        Optional<User> result = store.findUserByPhone(phone);

        Assertions.assertEquals(result, Optional.empty());
    }
}