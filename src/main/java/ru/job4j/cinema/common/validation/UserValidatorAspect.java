package ru.job4j.cinema.common.validation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.job4j.cinema.model.User;

import java.util.Optional;

/**
 * Аспект валидации входных данных пользователя.
 */
@Aspect
@Order(5)
@Component
public class UserValidatorAspect {
    private final Validator validator;

    public UserValidatorAspect(@Qualifier("userRequestValidator")
                                       Validator validator) {
        this.validator = validator;
    }

    @Pointcut("execution(public * "
            + "ru.job4j.cinema.service.UserService.add(ru.job4j.cinema.model.User)) "
            + "&& args(user)")
    public void callAdd(User user) {

    }

    @Pointcut("execution(public * "
            + "ru.job4j.cinema.service.UserService.findUserByPhone(java.lang.String)) "
            + "&& args(phone)")
    public void callFindUserByPhone(String phone) {

    }

    @Around("callAdd(user) && @annotation(validated)")
    public Optional<User> call(ProceedingJoinPoint jp,
                               CinemaValidated validated,
                               User user) throws Throwable {
        return validator.validate(jp, user);
    }

    @Around("callFindUserByPhone(phone) && @annotation(validated)")
    public Optional<User> call(ProceedingJoinPoint jp,
                               CinemaValidated validated,
                               String phone) throws Throwable {
        return validator.validate(jp, phone);
    }
}
