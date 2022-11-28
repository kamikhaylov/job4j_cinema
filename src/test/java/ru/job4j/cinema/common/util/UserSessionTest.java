package ru.job4j.cinema.common.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.User;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование утилитного класса создания пользовательской сессии
 */
class UserSessionTest {
    private static final String EMAIL = "test@test.ru";
    private static final String PHONE = "123";

    @Mock
    private Model model;
    @Mock
    private HttpSession httpSession;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() {
        Mockito.reset(model, httpSession);
    }

    @Test
    public void whenGetUser() {
        User user = new User(0, "user", EMAIL, PHONE);
        when(httpSession.getAttribute("user")).thenReturn(user);

        User actual = UserSession.getUser(model, httpSession);

        verify(model).addAttribute("user", user);
        Assertions.assertEquals(actual, user);
        Assertions.assertEquals(actual.getId(), user.getId());
        Assertions.assertEquals(actual.getUserName(), user.getUserName());
        Assertions.assertEquals(actual.getEmail(), user.getEmail());
        Assertions.assertEquals(actual.getPhone(), user.getPhone());
    }

    @Test
    public void whenGetGuest() {
        User user = new User(0, "guest", null, null);
        when(httpSession.getAttribute("user")).thenReturn(null);

        User actual = UserSession.getUser(model, httpSession);

        verify(model).addAttribute("user", user);
        Assertions.assertEquals(actual, user);
        Assertions.assertEquals(actual.getId(), user.getId());
        Assertions.assertEquals(actual.getUserName(), user.getUserName());
        Assertions.assertEquals(actual.getEmail(), user.getEmail());
        Assertions.assertEquals(actual.getPhone(), user.getPhone());
    }
}