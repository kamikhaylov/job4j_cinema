package ru.job4j.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Модель метрики мониторинга
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metric {
    /** Идентификатор */
    private int id;
    /** Дата и время записи */
    private LocalDateTime created;
    /** Код метрики */
    private String code;
    /** Название метрики */
    private String name;
    /** Значение метрики */
    private double value;
}
