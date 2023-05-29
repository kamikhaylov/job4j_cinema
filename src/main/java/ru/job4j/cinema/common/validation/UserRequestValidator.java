package ru.job4j.cinema.common.validation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.job4j.cinema.common.logging.CinemaLogger;
import ru.job4j.cinema.common.logging.CinemaLoggerFactory;
import ru.job4j.cinema.common.logging.LogEvent;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.LoggerService;

import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12013;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12023;
import static ru.job4j.cinema.common.validation.Validators.CHECK_PHONES;

/**
 * Валидатор входных параметров пользователя
 */
@Component
public class UserRequestValidator implements Validator {
    private static final Predicate<String> CHECK_PHONE =
            phone -> isNotEmpty(phone) && phone.matches(CHECK_PHONES.getValidator());

    private final CinemaLogger<LogEvent> logger;

    public UserRequestValidator(@Qualifier("loggerService") LoggerService loggerService) {
        logger = CinemaLoggerFactory.getLogger(UserRequestValidator.class, loggerService);
    }

    /**
     * Валидация входных параметров при регистрации пользователя
     * @param jp - метод
     * @param user - пользователь
     * @return возвращает пользователя
     */
    @Override
    public Optional<User> validate(ProceedingJoinPoint jp, User user) throws Throwable {
        if (nonNull(user) && isNotEmpty(user.getUserName()) && isNotEmpty(user.getEmail())
                && check(user.getPhone(), CHECK_PHONE)) {
            return (Optional<User>) jp.proceed();
        }
        logger.info(CINEMA12013);
        return Optional.empty();
    }

    /**
     * Валидация входных параметров при авторизации пользователя по телефону
     * @param jp - метод
     * @param phone - номер телефона пользователь
     * @return возвращает пользователя
     */
    @Override
    public Optional<User> validate(ProceedingJoinPoint jp, String phone) throws Throwable {
        if (check(phone, CHECK_PHONE)) {
            return (Optional<User>) jp.proceed();
        }
        logger.info(CINEMA12023);
        return Optional.empty();
    }

    private <T> boolean check(T value, Predicate<T> predicate) {
        return predicate.test(value);
    }
}
