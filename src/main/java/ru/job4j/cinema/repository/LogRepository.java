package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Log;

import java.util.List;

/**
 * Интерфейс взаимодействия с БД журналирования
 */
public interface LogRepository {

    /**
     * Добавление лога.
     * @param message - сообщение
     */
    void add(String level, String message, String className);

    /**
     * Добавление лога.
     * @param log - лог
     */
    void add(Log log);

    /**
     * Поиск всех логов.
     * @return возвращает логи
     */
    List<Log> findAll();

    /**
     * Очистка всех логов.
     */
    void clear();
}
