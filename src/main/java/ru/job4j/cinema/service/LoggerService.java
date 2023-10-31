package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Log;
import ru.job4j.cinema.repository.LogRepository;

/**
 * Сервис логера
 */
@ThreadSafe
@Service
public class LoggerService {
    private final LogRepository logRepository;

    public LoggerService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * Добавление лога.
     * @param message - сообщение
     */
    public void add(String level, String message, String className) {
        logRepository.add(level, message, className);
    }

    /**
     * Добавление лога.
     * @param log - лог
     */
    public void add(Log log) {
        logRepository.add(log);
    }
}
