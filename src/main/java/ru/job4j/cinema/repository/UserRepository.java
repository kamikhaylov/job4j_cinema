package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс работы с БД пользователей
 */
public interface UserRepository {

    /**
     * Добавлениие пользователя
     * @param user - пользователь
     * @return возвращает пользователя
     */
    Optional<User> add(User user);

    /**
     * Поиск пользователя по номеру телефона
     * @param phone - номер телефон
     * @return возвращает пользователя
     */
    Optional<User> findUserByPhone(String phone);

    /**
     * Поиск всех пользователей
     * @return возвращает список пользователей
     */
    List<User> findAll();
}
