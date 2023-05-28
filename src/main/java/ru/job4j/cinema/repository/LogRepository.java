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
     * Поиск всех логов.
     * @return возвращает логи
     */
    List<Log> findAll();

    /**
     * Очистка всех логов.
     */
    void clear();
}
