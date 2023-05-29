package ru.job4j.cinema.common.identification;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ExtendedModelMap;
import ru.job4j.cinema.AppTest;
import ru.job4j.cinema.config.DataSourceConfigH2;
import ru.job4j.cinema.controller.SessionController;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.LogRepository;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.job4j.cinema.common.identification.Roles.ADMIN;

/**
 * Тестирование аспека идентификации ролей
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppTest.class })
class CinemaIdentificationAspectTest {
    private static DataSource dataSource;

    @Autowired
    private SessionController sessionController;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void before() {
        dataSource = new DataSourceConfigH2().loadDataSource();
        logRepository.clear();
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

    /**
     * Если у пользователя роль user, то не может перейти в добавление киносеансов
     */
    @Test
    public void whenAddSessionThenRedirect() {
        String expected = "redirect:/sessions";
        Optional<User> user = userService.add(
                new User(0, "Konstantin", "mail@mail.ru", "12345678901"));
        userService.add(user.get());
        HttpSession httpSession = new MockHttpSession();
        httpSession.setAttribute("user", user.get());

        String actual = sessionController.addSession(new ExtendedModelMap(), httpSession);

        assertNotNull(actual);
        assertEquals(actual, expected);
    }

    /**
     * Если у пользователя роль admin, то доступно добавление киносеансов
     */
    @Test
    public void whenAddSessionThenAddSession() {
        String expected = "addSession";
        Optional<User> user = userService.add(
                new User(0, "Konstantin", "mail@mail.ru", "12345678901"));
        userService.updateRole(user.get().getId(), ADMIN.getCode());
        HttpSession httpSession = new MockHttpSession();
        httpSession.setAttribute("user", user.get());

        String actual = sessionController.addSession(new ExtendedModelMap(), httpSession);

        assertNotNull(actual);
        assertEquals(actual, expected);
    }
}