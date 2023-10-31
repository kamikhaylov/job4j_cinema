package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Role;
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
 * Работа с БД пользователей
 */
@Repository
public class CinemaUserRepository implements UserRepository {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(CinemaUserRepository.class.getName());

    private final DataSource dataSource;

    public CinemaUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Добавление пользователя
     * @param user - пользователь
     * @return возвращает пользователя
     */
    public Optional<User> add(User user) {
        LOGGER.info("UserDBStore.add");

        Optional<User> result = Optional.empty();
        try (Connection cn = dataSource.getConnection();
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
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users "
                     + "WHERE phone = ?")
        ) {
            ps.setString(1, phone);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    result = Optional.of(createUser(it));
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
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(createUser(it));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("UserStore.findAll.result : " + users.toString());
        return users;
    }

    /**
     * Добавление роли пользователя
     * @param id - идентификатор пользователь
     * @param name - название роли
     * @return возвращает признак успеха
     */
    public boolean addRole(int id, String name) {
        LOGGER.info("UserDBStore.addRole");

        boolean result = false;
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO roles(id, name) "
                             + "VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.execute();
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Обновление роли пользователя
     * @param id - идентификатор пользователь
     * @param name - название роли
     * @return возвращает признак успеха
     */
    public boolean updateRole(int id, String name) {
        LOGGER.info("UserDBStore.updateRole");

        boolean result = false;
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "UPDATE roles SET name = ? "
                             + "WHERE id = ?",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, name);
            ps.setInt(2, id);
            ps.execute();
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Получение роли пользователя
     * @param id - идентификатор пользователь
     * @return возвращает признак успеха
     */
    public Role getRole(int id) {
        LOGGER.info("UserDBStore.getRole");

        Role role = new Role();
        role.setId(id);
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM roles "
                     + "WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    role.setName(it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info("UserDBStore.role : " + role);
        return role;
    }

    private User createUser(ResultSet it) throws SQLException {
        return new User(
                it.getInt("id"),
                it.getString("userName"),
                it.getString("email"),
                it.getString("phone")
        );
    }
}
