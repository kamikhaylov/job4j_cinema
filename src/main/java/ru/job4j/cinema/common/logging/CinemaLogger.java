package ru.job4j.cinema.common.logging;

import org.slf4j.Logger;
import ru.job4j.cinema.service.LoggerService;

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

    public void info(T event) {
        if (logger.isInfoEnabled()) {
            try {
                logger.info(event.toString());
                loggerService.add(INFO, event.toString(), logger.getName());
            } catch (Exception exc) {
                logger.error(exc.getMessage());
                loggerService.add(ERROR, event.toString() + "." + exc.getMessage(), logger.getName());
            }
        }
    }

    public void error(T event, Throwable throwable) {
        logger.error(event.toString(), throwable);
        loggerService.add(ERROR, event.toString() + "." + throwable.getMessage(), logger.getName());
    }
}
