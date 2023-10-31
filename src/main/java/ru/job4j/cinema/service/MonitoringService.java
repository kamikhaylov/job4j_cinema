package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.common.monitoring.CinemaMonitoringPoint;
import ru.job4j.cinema.repository.MonitoringRepository;

/**
 * Сервис мониторинга
 */
@ThreadSafe
@Service
public class MonitoringService extends BaseMonitoringService {

    public MonitoringService(MonitoringRepository monitoringRepository) {
        super(monitoringRepository);
    }

    /**
     * Оборачивает мониторингом вызовы сервисов
     * @param point - точка мониторинга
     * @param jp - оборачиваемый метод
     * @return возвращает результат выполнения оборачиваемого метода
     */
    public Object wrap(CinemaMonitoringPoint point, ProceedingJoinPoint jp) throws Throwable {
        boolean success = false;
        long startTime = notifyStart(point.getStartEvent());
        try {
            Object result = jp.proceed();
            success = true;
            return result;
        } finally {
            notifyFinish(point, success, startTime);
        }
    }
}