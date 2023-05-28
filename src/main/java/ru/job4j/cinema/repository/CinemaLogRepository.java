package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Log;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Взаимодействие с БД журналирования
 */
@Repository
public class CinemaLogRepository implements LogRepository {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(CinemaLogRepository.class.getName());

    private final DataSource dataSource;

    public CinemaLogRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Добавление лога.
     * @param message - сообщение
     */
    @Override
    public void add(String level, String message, String className) {

        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO logs(level, created, message, class_name) "
                             + "VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, level);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, message);
            ps.setString(4, className);
            ps.execute();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Поиск всех логов
     * @return возвращает логи
     */
    @Override
    public List<Log> findAll() {
        List<Log> logs = new ArrayList<>();
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM logs")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    logs.add(createSession(it));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return logs;
    }

    /**
     * Очистка всех логов.
     */
    @Override
    public void clear() {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE FROM logs")
        ) {
            ps.execute();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private Log createSession(ResultSet it) throws SQLException {
        return new Log(
                it.getInt("id"),
                it.getString("level"),
                it.getTimestamp("created").toLocalDateTime(),
                it.getString("message"),
                it.getString("class_name")
        );
    }
}
