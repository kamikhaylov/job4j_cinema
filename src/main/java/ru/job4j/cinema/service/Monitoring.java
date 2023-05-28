package ru.job4j.cinema.service;

import ru.job4j.cinema.common.monitoring.Metrics;
import ru.job4j.cinema.common.monitoring.MonitoringPoint;

/**
 * Интерфейс сервиса по работе с мониторингом
 */
public interface Monitoring {

    /**
     * Уведомление о вызове сервиса.
     * @param metrics - метрика мониторинга
     * @return системное время на момент вызова (в мс)
     */
    long notifyStart(Metrics metrics);

    /**
     * Уведомление о завершении вызова сервиса.
     * @param point - точка мониторинга
     * @param success - true - успешный вызов, false - нет
     * @param requestTime - системное время в мс
     */
    void notifyFinish(MonitoringPoint point, boolean success, long requestTime);

    /**
     * Уведомление о длительности вызова сервиса.
     * @param metrics - метрика мониторинга
     * @param startTime - системное время в мс
     */
    void notifyDuration(Metrics metrics, long startTime);

    /**
     * Публикация события.
     * @param metrics - метрика мониторинга
     */
    void reportEvent(Metrics metrics);

    /**
     * Публикация события со значением.
     * @param metrics - метрика мониторинга
     * @param value - наблюдаемое значение
     */
    void reportEvent(Metrics metrics, double value);
}
