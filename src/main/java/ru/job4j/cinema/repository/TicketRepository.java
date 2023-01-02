package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс работы с БД билетов
 */
public interface TicketRepository {

    /**
     * Получение занятых рядов
     * @param session - сеанс кинотеатра
     * @param maxCellInRow - максимольное количество мест в ряду
     * @return возвращает список занятых рядов
     */
    List<Integer> getBusyRows(Session session, int maxCellInRow);

    /**
     * Получение занятых мест в ряду
     * @param session - сеанс кинотеатра
     * @param row - выбранный ряд
     * @return возвращает список занятых мест в ряду
     */
    List<Integer> getBusyCells(Session session, int row);


    /**
     * Добавление билета
     * @param session - сеанс кинотеатра
     * @param row - ряд
     * @param cell - место
     * @param user - пользователь
     * @return возвращает билет
     */
    Optional<Ticket> add(Session session, int row, int cell, User user);
}
