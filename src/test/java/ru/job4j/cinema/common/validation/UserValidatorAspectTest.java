package ru.job4j.cinema.common.validation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.job4j.cinema.AppTest;
import ru.job4j.cinema.config.DataSourceConfigH2;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Тестирование аспека валидатора
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppTest.class })
class UserValidatorAspectTest {
    private static DataSource dataSource;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void before() {
        dataSource = new DataSourceConfigH2().loadDataSource();
    }

    @AfterEach
    public void after() throws SQLException {
        try (PreparedStatement st = dataSource.getConnection().prepareStatement(
                "DELETE FROM users;"
                        + "ALTER TABLE users ALTER COLUMN id RESTART WITH 1;"
                        + "DELETE FROM roles;")) {
            st.execute();
        }
    }

    private static Stream<Arguments> phoneProvider() {
        return Stream.of(
                null,
                Arguments.of(""),
                Arguments.of("1"),
                Arguments.of("z"),
                Arguments.of("1234567890z"),
                Arguments.of("1234567890"),
                Arguments.of("123456789012")
        );
    }

    @ParameterizedTest
    @MethodSource("phoneProvider")
    public void whenFindUserByPhoneThenEmpty(String phone) {
        assertEquals(userService.findUserByPhone(phone), Optional.empty());
    }

    private static Stream<Arguments> userProvider() {
        return Stream.of(
                Arguments.of(createUser("Name", null, "email")),
                Arguments.of(createUser("Name", "", "email")),
                Arguments.of(createUser("Name", "1", "email")),
                Arguments.of(createUser("Name", "1234567890", "email")),
                Arguments.of(createUser("Name", "123456789012", "email")),
                Arguments.of(createUser("Name", "1234567890z", "email")),
                Arguments.of(createUser("", "12345678901", "email")),
                Arguments.of(createUser(null, "12345678901", "email")),
                Arguments.of(createUser("Name", "12345678901", "")),
                Arguments.of(createUser("Name", "12345678901", null))
        );
    }

    @ParameterizedTest
    @MethodSource("userProvider")
    public void whenAddUserThenEmpty(User user) {
        assertEquals(userService.add(user), Optional.empty());
    }

    @Test
    public void whenFindUserByPhoneThenUser() {
        String phone = "12345678901";
        User expected = new User();
        expected.setUserName("Konstantin");
        expected.setPhone(phone);
        expected.setEmail("mail@mail.ru");

        userService.add(expected);
        Optional<User> actual = userService.findUserByPhone(phone);

        assertNotEquals(actual, Optional.empty());
        assertEquals(expected.getUserName(), actual.get().getUserName());
        assertEquals(expected.getPhone(), actual.get().getPhone());
        assertEquals(expected.getEmail(), actual.get().getEmail());
    }

    private static User createUser(String name, String phone, String email) {
        User user = new User();
        user.setUserName(name);
        user.setPhone(phone);
        user.setEmail(email);
        return user;
    }
}