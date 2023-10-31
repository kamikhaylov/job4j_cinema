package ru.job4j.cinema.common.logging;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import ru.job4j.cinema.model.Log;
import ru.job4j.cinema.service.LoggerService;

import java.time.LocalDateTime;

/**
 * Логер кинотеатра.
 */
public class CinemaLogger<T extends LogEvent> {
    private static final String INFO = "INFO";
    private static final String ERROR = "ERROR";

    private final Logger logger;
    private final LoggerService loggerService;

    public CinemaLogger(Logger logger, LoggerService loggerService) {
        this.logger = logger;
        this.loggerService = loggerService;
    }

    /**
     * Логирует события уровня INFO.
     */
    public void info(T event) {
        if (logger.isInfoEnabled()) {
            try {
                logger.info(event.toString());
                loggerService.add(INFO, event.toString(), logger.getName());
            } catch (Exception exc) {
                error(event, exc);
            }
        }
    }

    /**
     * Логирует события уровня ERROR.
     */
    public void error(T event, Throwable throwable) {
        logger.error(event.toString(), throwable);
        loggerService.add(new Log(0, ERROR, LocalDateTime.now(),
                event.toString(), logger.getName(), ExceptionUtils.getStackTrace(throwable)
        ));
    }
}
