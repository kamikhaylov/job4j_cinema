package ru.job4j.cinema.common.validation;

import org.aspectj.lang.ProceedingJoinPoint;
import ru.job4j.cinema.model.User;

import java.util.Optional;

/**
 * Интерфейс валидатора
 */
public interface Validator {

    /**
     * Валидция входных параметров при регистрации пользователя
     * @param jp - метод
     * @param user - пользователь
     * @return возвращает пользователя
     */
    Optional<User> validate(ProceedingJoinPoint jp, User user) throws Throwable;

    /**
     * Валидция входных параметров при авторизации пользователя по телефону
     * @param jp - метод
     * @param phone - номер телефона пользователь
     * @return возвращает пользователя
     */
    Optional<User> validate(ProceedingJoinPoint jp, String phone) throws Throwable;
}
