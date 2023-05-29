package ru.job4j.cinema.common.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.job4j.cinema.service.LoggerService;

/**
 * Аспект обработки результата выполнения сервиса: логирование.
 */
@Aspect
@Order(10)
@Component
public class CinemaLoggingAspect {
    private final CinemaLogger<LogEvent> logger;

    public CinemaLoggingAspect(@Qualifier("loggerService") LoggerService loggerService) {
        logger = CinemaLoggerFactory.getLogger(CinemaLoggingAspect.class, loggerService);
    }

    @Pointcut("execution(public * ru.job4j.cinema.controller..*Controller*.*(..))")
    public void callService() {

    }

    @Around("callService() && @annotation(logged)")
    public Object call(ProceedingJoinPoint jp, CinemaLogged logged) throws Throwable {
        Object response = null;
        try {
            logger.info(logged.start());
            response = jp.proceed();
            logger.info(logged.success());
        } catch (Exception exc) {
            logger.error(logged.fail(), exc);
        }
        return response;
    }
}
