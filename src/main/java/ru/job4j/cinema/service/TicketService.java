package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.TicketStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис билетов
 */
@ThreadSafe
@Service
public class TicketService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketService.class.getName());
    private static final int MAX_CELL_IN_ROW = 3;

    private final TicketStore ticketStore;

    public TicketService(TicketStore ticketStore) {
        this.ticketStore = ticketStore;
    }

    /**
     * Получение свободных рядов
     * @param session - сеанс кинотеатра
     * @return возвращает список свободных рядов
     */
    public List<Integer> getRows(Session session) {
        List<Integer> busyRows = ticketStore.getBusyRows(session, MAX_CELL_IN_ROW);
        List<Integer> rows = createRows();
        rows.removeAll(busyRows);

        LOGGER.info("TicketService.getRows.rows : " + rows);
        return rows;
    }

    /**
     * Получение свободных мест в ряду
     * @param session - сеанс кинотеатра
     * @param row - выбранный ряд
     * @return возвращает список свободных мест
     */
    public List<Integer> getCells(Session session, int row) {
        List<Integer> busyCells = ticketStore.getBusyCells(session, row);
        List<Integer> cells = createCells();
        cells.removeAll(busyCells);

        LOGGER.info("TicketService.getCells.cells : " + cells);
        return cells;
    }

    /**
     * Создание билета
     * @param session - сеанс кинотеатра
     * @param row - ряд
     * @param cell - место
     * @param user - пользователь
     * @return возвращает билет
     */
    public Optional<Ticket> createTicket(Session session, int row, int cell, User user) {
        LOGGER.info("TicketService.createTicket : " + session
                + ", row : " + row + ", cell : " + cell + ", user : " + user);
        return ticketStore.add(session, row, cell, user);
    }

    private List<Integer> createRows() {
        return new ArrayList<>(List.of(1, 2, 3, 4, 5));
    }

    private List<Integer> createCells() {
        return new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
    }

}
