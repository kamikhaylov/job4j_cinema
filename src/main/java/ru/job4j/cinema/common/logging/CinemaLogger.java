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
        String message = createMessage(event);
        if (logger.isInfoEnabled()) {
            try {
                logger.info(message);
                loggerService.add(INFO, message, logger.getName());
            } catch (Exception exc) {
                logger.error(exc.getMessage());
                loggerService.add(ERROR, message + "." + exc.getMessage(), logger.getName());
            }
        }
    }

    public void error(T event, Throwable throwable) {
        String message = createMessage(event);
        logger.error(message, throwable);
        loggerService.add(ERROR, message + "." + throwable.getMessage(), logger.getName());
    }

    private String createMessage(T event) {
        return event.getCode() + "." + event.getMessage();
    }
}
