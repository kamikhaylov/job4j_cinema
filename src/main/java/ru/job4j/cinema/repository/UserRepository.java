package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Role;
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

    /**
     * Добавление роли пользователя
     * @param id - идентификатор пользователь
     * @param name - название роли
     * @return возвращает признак успеха
     */
    boolean addRole(int id, String name);


    /**
     * Обновление роли пользователя
     * @param id - идентификатор пользователь
     * @param name - название роли
     * @return возвращает признак успеха
     */
    boolean updateRole(int id, String name);

    /**
     * Получение роли пользователя
     * @param id - идентификатор пользователь
     * @return возвращает признак успеха
     */
    Role getRole(int id);
}
