package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.common.logging.CinemaLogged;
import ru.job4j.cinema.common.monitoring.CinemaMonitored;
import ru.job4j.cinema.common.util.UserSession;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12000;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12001;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12002;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12010;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12011;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12012;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12020;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12021;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12022;
import static ru.job4j.cinema.common.monitoring.CinemaMonitoringPoint.CINEMA_ADD_USER;
import static ru.job4j.cinema.common.monitoring.CinemaMonitoringPoint.CINEMA_LOGIN;
import static ru.job4j.cinema.common.monitoring.CinemaMonitoringPoint.CINEMA_USER_REGISTRATION;

/**
 * Контроллер пользователей
 */
@ThreadSafe
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Страница регистрации пользователя.
     * @param model - модель данных
     * @return возвращает страницу регистрации
     */
    @GetMapping("/formAddUser")
    @CinemaLogged(start = CINEMA12000, success = CINEMA12001, fail = CINEMA12002)
    @CinemaMonitored(value = CINEMA_ADD_USER)
    public String addUser(Model model) {
        model.addAttribute("user", new User(0, "", "", ""));
        return "addUser";
    }

    /**
     * Добавление пользователя.
     * @param model - модель данных
     * @param user - пользователь
     * @return результат
     */
    @PostMapping("/registration")
    @CinemaLogged(start = CINEMA12010, success = CINEMA12011, fail = CINEMA12012)
    @CinemaMonitored(value = CINEMA_USER_REGISTRATION)
    public String registration(Model model, @ModelAttribute User user) {
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            model.addAttribute("message", "");
            return "redirect:/userFail";
        }
        return "redirect:/userSuccess";
    }

    /**
     * Страница - Пользователь успешно зарегистрирован.
     * @param user - пользователь
     * @return возвращает страницу с успешной регистрацией
     */
    @GetMapping("/userSuccess")
    public String userSuccess(@ModelAttribute User user) {
        return "userSuccess";
    }

    /**
     * Страница - Пользователь существует.
     * @param user - пользователь
     * @return возвращает страницу с ошибкой
     */
    @GetMapping("/userFail")
    public String userFail(@ModelAttribute User user) {
        return "userFail";
    }

    /**
     * Страница авторизации.
     * @param model - модель данных
     * @param fail - признак ошибки в авторизации
     * @return возвращает страницу авторизации
     */
    @GetMapping("/loginPage")
    public String loginPage(Model model,
                            @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    /**
     * Авторизация.
     * @param user - пользователь
     * @param req - запрос
     * @return возвращает результат авторизации
     */
    @PostMapping("/login")
    @CinemaLogged(start = CINEMA12020, success = CINEMA12021, fail = CINEMA12022)
    @CinemaMonitored(value = CINEMA_LOGIN)
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = userService.findUserByPhone(user.getPhone());
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        UserSession.create(userDb.get(), req);
        return "redirect:/sessions";
    }

    /**
     * Выход.
     * @param session - сессия
     * @return возвращает страницу авторизации
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        UserSession.invalidate(session);
        return "redirect:/loginPage";
    }
}
