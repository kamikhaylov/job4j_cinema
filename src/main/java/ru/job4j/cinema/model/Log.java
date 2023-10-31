package ru.job4j.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Модель лога
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    /** Идентификатор */
    private int id;
    /** Уровень логирования */
    private String level;
    /** Дата и время записи */
    private LocalDateTime created;
    /** Запись журнала */
    private String message;
    /** Имя класса */
    private String className;
    /** Трассировка */
    private String trace;
}
