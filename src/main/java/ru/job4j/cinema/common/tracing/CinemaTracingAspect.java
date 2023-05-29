package ru.job4j.cinema.common.tracing;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.job4j.cinema.common.logging.CinemaLogger;
import ru.job4j.cinema.common.logging.CinemaLoggerFactory;
import ru.job4j.cinema.common.logging.LogEvent;
import ru.job4j.cinema.common.validation.UserRequestValidator;
import ru.job4j.cinema.service.LoggerService;

/**
 * Аспект обработки исключений.
 */
@Aspect
@Order(10)
@Component
public class CinemaTracingAspect {
    private final CinemaLogger<LogEvent> logger;

    public CinemaTracingAspect(@Qualifier("loggerService") LoggerService loggerService) {
        logger = CinemaLoggerFactory.getLogger(UserRequestValidator.class, loggerService);
    }

    @Pointcut("execution(public * ru.job4j.cinema.controller..*Controller*.*(..))")
    public void methodExecuting() {

    }

    @AfterThrowing(value = "methodExecuting() && @annotation(traceable)", throwing = "exception")
    public void recordFailedExecution(TraceableException traceable, Exception exception) {
        logger.error(traceable.event(), exception);
    }
}
