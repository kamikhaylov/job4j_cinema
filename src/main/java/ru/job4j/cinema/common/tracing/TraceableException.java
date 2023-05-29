package ru.job4j.cinema.common.tracing;

import ru.job4j.cinema.common.logging.CinemaLogEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация аспекта для отслеживания исключений.
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TraceableException {

    /** Событие */
    CinemaLogEvent event();
}
