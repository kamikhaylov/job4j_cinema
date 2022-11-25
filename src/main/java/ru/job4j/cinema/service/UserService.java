package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.common.model.User;
import ru.job4j.cinema.persistence.UserStore;

import java.util.Optional;

/**
 * Сервис пользователей
 */
@ThreadSafe
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class.getName());

    private final UserStore store;

    public UserService(UserStore store) {
        this.store = store;
    }

    /**
     * Добавление нового пользователя
     * @param user - пользователь
     * @return возвращает пользователя
     */
    public Optional<User> add(User user) {
        LOGGER.info("UserService.add");
        return store.add(user);
    }

    /**
     * Поиск пользователя по номеру телефона
     * @param phone - номер телефон
     * @return возвращает пользователя
     */
    public Optional<User> findUserByPhone(String phone) {
        LOGGER.info("UserService.findUserByPhone : " + phone);
        return store.findUserByPhone(phone);
    }
}
