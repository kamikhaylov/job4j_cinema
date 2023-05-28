package ru.job4j.cinema.service;

import ru.job4j.cinema.common.monitoring.Metrics;
import ru.job4j.cinema.common.monitoring.MonitoringPoint;
import ru.job4j.cinema.repository.MonitoringRepository;

/**
 * Базовый сервис мониторинга
 */
public class BaseMonitoringService implements Monitoring {
    private final MonitoringRepository monitoringRepository;

    public BaseMonitoringService(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }

    /**
     * Уведомление о вызове сервиса.
     * @param metrics - метрика мониторинга
     * @return системное время на момент вызова (в мс)
     */
    @Override
    public long notifyStart(Metrics metrics) {
        reportEvent(metrics);
        return System.currentTimeMillis();
    }

    /**
     * Уведомление о завершении вызова сервиса.
     * @param point - точка мониторинга
     * @param success - true - успешный вызов, false - нет
     * @param requestTime - системное время в мс
     */
    @Override
    public void notifyFinish(MonitoringPoint point, boolean success, long requestTime) {
        notifyDuration(point.getDurationEvent(), requestTime);
        if (success) {
            reportEvent(point.getSuccessEvent());
        } else {
            reportEvent(point.getErrorEvent());
        }
    }

    /**
     * Уведомление о длительности вызова сервиса.
     * @param metrics - метрика мониторинга
     * @param startTime - системное время в мс
     */
    @Override
    public void notifyDuration(Metrics metrics, long startTime) {
        monitoringRepository.reportEvent(metrics, System.currentTimeMillis() - startTime);
    }

    /**
     * Публикация события.
     * @param metrics - метрика мониторинга
     */
    @Override
    public void reportEvent(Metrics metrics) {
        reportEvent(metrics, 1.0D);
    }

    /**
     * Публикация события со значением.
     * @param metrics - метрика мониторинга
     * @param value - наблюдаемое значение
     */
    @Override
    public void reportEvent(Metrics metrics, double value) {
        monitoringRepository.reportEvent(metrics, value);
    }
}
