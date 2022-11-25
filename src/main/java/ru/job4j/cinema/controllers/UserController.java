package ru.job4j.cinema.controllers;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.common.model.User;
import ru.job4j.cinema.common.util.UserSession;
import ru.job4j.cinema.service.UserService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Контроллер пользователей
 */
@ThreadSafe
@Controller
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class.getName());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/formAddUser")
    public String addUser(Model model) {
        LOGGER.info("UserController.addUser");
        model.addAttribute("user", new User(0, "", "", ""));
        return "addUser";
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute User user) {
        LOGGER.info("UserController.registration");

        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            model.addAttribute("message", "");
            return "redirect:/userFail";
        }
        return "redirect:/userSuccess";
    }

    @GetMapping("/userSuccess")
    public String userSuccess(@ModelAttribute User user) {
        LOGGER.info("UserController.success");
        return "userSuccess";
    }

    @GetMapping("/userFail")
    public String userFail(@ModelAttribute User user) {
        LOGGER.info("UserController.fail");
        return "userFail";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model,
                            @RequestParam(name = "fail", required = false) Boolean fail) {
        LOGGER.info("UserController.loginPage");
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        LOGGER.info("UserController.login");
        Optional<User> userDb = userService.findUserByPhone(user.getPhone());
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        UserSession.create(userDb.get(), req);
        return "redirect:/sessions";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        UserSession.invalidate(session);
        return "redirect:/loginPage";
    }
}
