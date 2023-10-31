package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.common.validation.CinemaValidated;
import ru.job4j.cinema.model.Role;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.Optional;

import static ru.job4j.cinema.common.identification.Roles.USER;

/**
 * Сервис пользователей
 */
@ThreadSafe
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class.getName());

    private final UserRepository store;

    public UserService(UserRepository store) {
        this.store = store;
    }

    /**
     * Добавление нового пользователя
     * @param user - пользователь
     * @return возвращает пользователя
     */
    @CinemaValidated
    public Optional<User> add(User user) {
        LOGGER.info("UserService.add");
        Optional<User> result = store.add(user);
        result.ifPresent(value -> addRole(value.getId(), USER.getCode()));
        return result;
    }

    /**
     * Поиск пользователя по номеру телефона
     * @param phone - номер телефон
     * @return возвращает пользователя
     */
    @CinemaValidated
    public Optional<User> findUserByPhone(String phone) {
        LOGGER.info("UserService.findUserByPhone : " + phone);
        return store.findUserByPhone(phone);
    }

    /**
     * Добавление роли пользователя
     * @param id - идентификатор пользователь
     * @param name - название роли
     * @return возвращает признак успеха
     */
    public boolean addRole(int id, String name) {
        boolean result = false;
        if (id != 0) {
            result = store.addRole(id, name);
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
        boolean result = false;
        if (id != 0) {
            result = store.updateRole(id, name);
        }
        return result;
    }

    /**
     * Получение роли пользователя
     * @param id - идентификатор пользователь
     * @return возвращает признак успеха
     */
    public Role getRole(int id) {
        return store.getRole(id);
    }
}
