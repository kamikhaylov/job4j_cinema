package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.common.model.Session;
import ru.job4j.cinema.persistence.SessionStore;

import java.util.Collection;
import java.util.List;

/**
 * Сервис сеансов кинотеатра
 */
@ThreadSafe
@Service
public class SessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class.getName());

    private final SessionStore sessionStore;

    public SessionService(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    /**
     * Поиск всех сеансов кинотеатра
     * @return возвращает сеансы
     */
    public Collection<Session> findAll() {
        LOGGER.info("SessionService.findAll");
        List<Session> sessions = sessionStore.findAll();
        return sessions;
    }

    /**
     * Добавление сеанса кинотеатра
     * @param session - сеанс кинотеатра
     * @return возвращает результат добавления
     */
    public boolean add(Session session) {
        LOGGER.info("SessionService.add");
        return sessionStore.add(session);
    }

    /**
     * Поиск сеанса кинотеатра по id
     * @param id - идентификатор сеанса
     * @return возвращает сеанс
     */
    public Session findById(int id) {
        LOGGER.info("SessionService.findById");
        return sessionStore.findById(id);
    }
}
