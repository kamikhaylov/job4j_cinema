package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Работа с БД билетов
 */
@Repository
public class CinemaTicketRepository implements TicketRepository {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(CinemaTicketRepository.class.getName());

    private final DataSource dataSource;

    public CinemaTicketRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Получение занятых рядов
     * @param session - сеанс кинотеатра
     * @param maxCellInRow - максимольное количество мест в ряду
     * @return возвращает список занятых рядов
     */
    public List<Integer> getBusyRows(Session session, int maxCellInRow) {
        LOGGER.info("TicketStore.getBusyRows.session : " + session
                + ", maxCellInRow : " + maxCellInRow);

        List<Integer> rows = new ArrayList<>();
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "SELECT pos_row FROM tickets "
                             + "WHERE session_id = ? "
                             + "GROUP BY pos_row "
                             + "HAVING COUNT(cell) = ?")
        ) {
            ps.setInt(1, session.getId());
            ps.setInt(2, maxCellInRow);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    rows.add(createTicket(it, "pos_row"));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info("TicketStore.getBusyRows.result : " + rows);
        return rows;
    }

    /**
     * Получение занятых мест в ряду
     * @param session - сеанс кинотеатра
     * @param row - выбранный ряд
     * @return возвращает список занятых мест в ряду
     */
    public List<Integer> getBusyCells(Session session, int row) {
        LOGGER.info("TicketStore.getBusyCells.session : " + session + ", row : " + row);

        List<Integer> cells = new ArrayList<>();
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "SELECT cell FROM tickets "
                             + "WHERE session_id = ? AND pos_row = ?")
        ) {
            ps.setInt(1, session.getId());
            ps.setInt(2, row);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cells.add(createTicket(it, "cell"));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info("TicketStore.getBusyCells.result : " + cells);
        return cells;
    }

    /**
     * Добавление билета
     * @param session - сеанс кинотеатра
     * @param row - ряд
     * @param cell - место
     * @param user - пользователь
     * @return возвращает билет
     */
    public Optional<Ticket> add(Session session, int row, int cell, User user) {
        LOGGER.info("TicketStore.add");

        Ticket ticket = new Ticket(0, session.getId(), row, cell, user.getId());
        Optional<Ticket> result = Optional.empty();

        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO tickets(session_id, pos_row, cell, user_id) "
                             + "VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, session.getId());
            ps.setInt(2, row);
            ps.setInt(3, cell);
            ps.setInt(4, user.getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                    result = Optional.of(ticket);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return result;
    }

    private Integer createTicket(ResultSet it, String columnLabel) throws SQLException {
        return it.getInt(columnLabel);
    }
}
