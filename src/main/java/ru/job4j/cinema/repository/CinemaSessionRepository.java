package ru.job4j.cinema.repository;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Работа с БД сеансов кинотеатра
 */
@Repository
public class CinemaSessionRepository implements SessionRepository {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(CinemaSessionRepository.class.getName());
    private final DataSource dataSource;

    public CinemaSessionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Поиск всех сеансов кинотеатра
     * @return возвращает сеансы
     */
    public List<Session> findAll() {
        LOGGER.info("SessionStore.findAll");

        List<Session> sessions = new ArrayList<>();
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM sessions")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    sessions.add(createSession(it));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("SessionStore.findAll.result : " + sessions.toString());
        return sessions;
    }

    /**
     * Добавление сеанса кинотеатра
     * @param session - сеанс кинотеатра
     * @return возвращает результат добавления
     */
    public boolean add(Session session) {
        LOGGER.info("SessionStore.add");

        boolean result = false;
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO sessions(name, poster) "
                             + "VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, session.getName());
            ps.setString(2, encodeBase64(session.getPoster()));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    session.setId(id.getInt(1));
                }
            }
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("SessionStore.add.result : " + result);
        return result;
    }

    /**
     * Поиск сеанса кинотеатра по id
     * @param id - идентификатор сеанса
     * @return возвращает сеанс
     */
    public Session findById(int id) {
        LOGGER.info("SessionStore.findById.id : " + id);

        Session session = null;
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM sessions WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    session = createSession(it);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info("SessionStore.findById.session : " + session);
        return session;
    }

    private String encodeBase64(byte[] poster) {
        Base64 base64 = new Base64();
        return Objects.nonNull(poster) ? base64.encodeToString(poster) : "";
    }

    private byte[] decodeBase64(String poster) {
        Base64 base64 = new Base64();
        return StringUtils.isNotEmpty(poster) ? base64.decode(poster) : null;
    }

    private Session createSession(ResultSet it) throws SQLException {
        return new Session(
                it.getInt("id"),
                it.getString("name"),
                decodeBase64(it.getString("poster"))
        );
    }
}