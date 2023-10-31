package ru.job4j.cinema.common.logging;

import org.slf4j.LoggerFactory;
import ru.job4j.cinema.service.LoggerService;

/**
 * Фабрика логера кинотеатра.
 */
public class CinemaLoggerFactory {

    private CinemaLoggerFactory() {
    }

    public static <T extends LogEvent> CinemaLogger<T> getLogger(Class<?> type,
                                                                 LoggerService loggerService) {
        return new CinemaLogger<>(LoggerFactory.getLogger(type), loggerService);
    }
}
