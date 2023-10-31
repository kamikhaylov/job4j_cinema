package ru.job4j.cinema.common.monitoring;

/**
 * Информация о метрики.
 */
public interface Metrics {
    /**
     * Код метрики.
     * @return код события
     */
    String getCode();

    /**
     * Заголовок.
     * @return заголовок метрики
     */
    String getTitle();
}
