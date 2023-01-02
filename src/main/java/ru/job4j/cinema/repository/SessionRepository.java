package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Session;

import java.util.List;

/**
 * Интерфейс работы с БД сеансов кинотеатра
 */
public interface SessionRepository {

    /**
     * Поиск всех сеансов кинотеатра
     * @return возвращает сеансы
     */
    List<Session> findAll();

    /**
     * Добавление сеанса кинотеатра
     * @param session - сеанс кинотеатра
     * @return возвращает результат добавления
     */
    boolean add(Session session);

    /**
     * Поиск сеанса кинотеатра по id
     * @param id - идентификатор сеанса
     * @return возвращает сеанс
     */
    Session findById(int id);
}