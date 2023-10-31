package ru.job4j.cinema.common.monitoring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.job4j.cinema.service.MonitoringService;

/**
 * Аспект мониторинга сервисов и времени выполнения событий.
 */
@Aspect
@Order(0)
@Component
public class CinemaMonitoringAspect {
    private final MonitoringService monitoringService;

    public CinemaMonitoringAspect(@Qualifier("monitoringService")
                                          MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @Pointcut("execution(public * ru.job4j.cinema.controller..*Controller*.*(..))")
    public void callService() {

    }

    @Around("callService() && @annotation(monitored)")
    public Object call(ProceedingJoinPoint jp, CinemaMonitored monitored) throws Throwable {
        return monitoringService.wrap(monitored.value(), jp);
    }
}
