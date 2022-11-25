package ru.job4j.cinema.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import ru.job4j.cinema.common.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестрованиие контроллера пользователей
 */
class UserControllerTest {
    private static final String EMAIL = "test@test.ru";
    private static final String PHONE = "123";

    @Mock
    private Model model;
    @Mock
    private UserService userService;
    @Mock
    private HttpSession httpSession;
    @Mock
    private HttpServletRequest req;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() {
        Mockito.reset(model, userService, httpSession, req);
    }

    @Test
    public void whenAddUser() {
        UserController userController = new UserController(userService);

        String page = userController.addUser(model);

        verify(model).addAttribute("user", new User(0, "", "", ""));
        Assertions.assertEquals(page, "addUser");
    }

    @Test
    public void whenRegistrationFail() {
        UserController userController = new UserController(userService);
        User user = new User(0, "user", EMAIL, PHONE);
        when(userService.add(user)).thenReturn(Optional.empty());

        String page = userController.registration(model, user);

        verify(userService).add(user);
        verify(model).addAttribute("message", "");
        Assertions.assertEquals(page, "redirect:/userFail");
    }

    @Test
    public void whenRegistrationSuccess() {
        UserController userController = new UserController(userService);
        User user = new User(0, "user", EMAIL, PHONE);
        when(userService.add(user)).thenReturn(Optional.of(user));

        String page = userController.registration(model, user);

        verify(userService).add(user);
        Assertions.assertEquals(page, "redirect:/userSuccess");
    }

    @Test
    public void whenUserSuccess() {
        UserController userController = new UserController(userService);
        User user = new User(0, "user", EMAIL, PHONE);

        String page = userController.userSuccess(user);

        Assertions.assertEquals(page, "userSuccess");
    }

    @Test
    public void whenUserFail() {
        UserController userController = new UserController(userService);
        User user = new User(0, "user", EMAIL, PHONE);

        String page = userController.userFail(user);

        Assertions.assertEquals(page, "userFail");
    }

    @Test
    public void whenLoginPage() {
        UserController userController = new UserController(userService);

        String page = userController.loginPage(model, true);

        verify(model).addAttribute("fail", true);
        Assertions.assertEquals(page, "login");
    }

    @Test
    public void whenLoginFail() {
        UserController userController = new UserController(userService);
        User user = new User(0, "user", EMAIL, PHONE);
        when(userService.findUserByPhone(PHONE)).thenReturn(Optional.empty());

        String page = userController.login(user, req);

        verify(userService).findUserByPhone(PHONE);
        Assertions.assertEquals(page, "redirect:/loginPage?fail=true");
    }

    @Test
    public void whenLogin() {
        UserController userController = new UserController(userService);
        User user = new User(0, "user", EMAIL, PHONE);
        when(userService.findUserByPhone(PHONE)).thenReturn(Optional.of(user));
        when(req.getSession()).thenReturn(httpSession);

        String page = userController.login(user, req);

        verify(userService).findUserByPhone(PHONE);
        Assertions.assertEquals(page, "redirect:/sessions");
    }

    @Test
    public void whenLogout() {
        UserController userController = new UserController(userService);

        String page = userController.logout(httpSession);

        verify(httpSession).invalidate();
        Assertions.assertEquals(page, "redirect:/loginPage");
    }
}