package ru.job4j.cinema.common.identification;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.job4j.cinema.common.logging.CinemaLogger;
import ru.job4j.cinema.common.logging.CinemaLoggerFactory;
import ru.job4j.cinema.common.logging.LogEvent;
import ru.job4j.cinema.common.util.UserSession;
import ru.job4j.cinema.common.validation.UserRequestValidator;
import ru.job4j.cinema.model.Role;
import ru.job4j.cinema.service.LoggerService;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpSession;

import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA10023;

/**
 * Аспект идентификации ролей пользователей.
 */
@Aspect
@Order(5)
@Component
public class CinemaIdentificationAspect {
    private final CinemaLogger<LogEvent> logger;

    @Qualifier
    private final UserService userService;

    public CinemaIdentificationAspect(UserService userService,
                                      @Qualifier("loggerService")
                                              LoggerService loggerService) {
        this.userService = userService;
        logger = CinemaLoggerFactory.getLogger(UserRequestValidator.class, loggerService);
    }

    @Pointcut("execution(public * "
            + "ru.job4j.cinema.controller.SessionController."
            + "addSession(org.springframework.ui.Model, javax.servlet.http.HttpSession)) "
            + "&& args(model, httpSession)")
    public void callAddSession(Model model, HttpSession httpSession) {

    }

    @Around("callAddSession(model, httpSession) && @annotation(identified)")
    public Object call(ProceedingJoinPoint jp,
                       RoleIdentified identified,
                       Model model, HttpSession httpSession) throws Throwable {
        Role role = userService.getRole(UserSession.getUser(model, httpSession).getId());
        if (role.getName().equals(identified.role().getCode())) {
            return jp.proceed();
        }
        logger.info(CINEMA10023);
        return "redirect:/sessions";
    }
}
