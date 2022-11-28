package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Работа с БД пользователей
 */
@Repository
public class UserStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserStore.class.getName());

    private final BasicDataSource pool;

    public UserStore(BasicDataSource pool) {
        this.pool = pool;
    }

    /**
     * Добавлениие пользователя
     * @param user - пользователь
     * @return возвращает пользователя
     */
    public Optional<User> add(User user) {
        LOGGER.info("UserDBStore.add");

        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO users(username, email, phone) "
                             + "VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                    result = Optional.of(user);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Поиск пользователя по номеру телефона
     * @param phone - номер телефон
     * @return возвращает пользователя
     */
    public Optional<User> findUserByPhone(String phone) {
        LOGGER.info("UserStore.findUserByPhone : " + phone);

        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users "
                     + "WHERE phone = ?")
        ) {
            ps.setString(1, phone);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    result = Optional.of(new User(
                            it.getInt("id"),
                            it.getString("username"),
                            it.getString("email"),
                            it.getString("phone"))
                    );
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info("UserStore.findUserByPhone.user : " + result);
        return result;
    }

    /**
     * Поиск всех пользователей
     * @return возвращает список пользователей
     */
    public List<User> findAll() {
        LOGGER.info("UserStore.findAll");

        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(new User(
                            it.getInt("id"),
                            it.getString("userName"),
                            it.getString("email"),
                            it.getString("phone"))
                    );
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("UserStore.findAll.result : " + users.toString());
        return users;
    }
}
