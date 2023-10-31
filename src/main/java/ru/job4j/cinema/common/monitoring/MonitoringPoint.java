package ru.job4j.cinema.common.monitoring;

/**
 * Метрики для точки мониторинга.
 */
public interface MonitoringPoint {

    /**
     * Метрика начала операции.
     * @return метрика начала операци
     */
    Metrics getStartEvent();

    /**
     * Метрика длительности операции.
     * @return метрика длительности операции
     */
    Metrics getDurationEvent();

    /**
     * Метрика успешного завершения операции.
     * @return метрика успешного завершения операции
     */
    Metrics getSuccessEvent();

    /**
     * Метрика завершения операции с ошибкой.
     * @return метрика завершения операции с ошибкой
     */
    Metrics getErrorEvent();
}
