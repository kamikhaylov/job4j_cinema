package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.common.monitoring.Metrics;
import ru.job4j.cinema.model.Metric;

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
 * Взаимодействие с БД мониторинга
 */
@Repository
public class CinemaMonitoringRepository implements MonitoringRepository {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(CinemaMonitoringRepository.class.getName());

    private final DataSource dataSource;

    public CinemaMonitoringRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Публикация события со значением.
     * @param metrics - метрика мониторинга
     * @param value - наблюдаемое значение
     */
    @Override
    public void reportEvent(Metrics metrics, double value) {
        LOGGER.info(metrics.toString() + ", value = " + value);
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO metrics(created, code, name, value) "
                             + "VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, metrics.getCode());
            ps.setString(3, metrics.getTitle());
            ps.setDouble(4, value);
            ps.execute();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * Поиск всех метрик
     * @return возвращает метрики
     */
    @Override
    public List<Metric> findAll() {
        List<Metric> metrics = new ArrayList<>();
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM metrics")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    metrics.add(createMetric(it));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return metrics;
    }

    /**
     * Очистка всех метрик.
     */
    @Override
    public void clear() {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE FROM metrics")
        ) {
            ps.execute();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    private Metric createMetric(ResultSet it) throws SQLException {
        return new Metric(
                it.getInt("id"),
                it.getTimestamp("created").toLocalDateTime(),
                it.getString("code"),
                it.getString("name"),
                it.getDouble("value")
        );
    }
}
