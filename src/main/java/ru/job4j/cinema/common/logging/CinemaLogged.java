package ru.job4j.cinema.common.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для логирования результата выполнения сервиса кинотеатра.
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CinemaLogged {

    /** Вызов сервиса */
    CinemaLogEvent start();

    /** Успешное завершение */
    CinemaLogEvent success();

    /** Ошибкаа в ответе сервиса */
    CinemaLogEvent fail();
}
