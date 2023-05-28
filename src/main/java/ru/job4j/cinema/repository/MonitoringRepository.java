package ru.job4j.cinema.repository;

import ru.job4j.cinema.common.monitoring.Metrics;
import ru.job4j.cinema.model.Metric;

import java.util.List;

/**
 * Интерфейс взаимодействия с БД мониторинга
 */
public interface MonitoringRepository {

    /**
     * Публикация события со значением.
     * @param metrics - метрика мониторинга
     * @param value - наблюдаемое значение
     */
    void reportEvent(Metrics metrics, double value);

    /**
     * Поиск всех метрик
     * @return возвращает метрики
     */
    List<Metric> findAll();

    /**
     * Очистка всех метрик.
     */
    void clear();
}
