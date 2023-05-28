package ru.job4j.cinema.common.logging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import ru.job4j.cinema.AppTest;
import ru.job4j.cinema.controller.UserController;
import ru.job4j.cinema.model.Log;
import ru.job4j.cinema.repository.LogRepository;

import java.util.List;

import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12000;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA12001;

/**
 * Тестирование аспека Журналирования сервисов
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppTest.class })
class CinemaLoggingAspectTest {

    @Autowired
    private UserController userController;

    @Autowired
    private LogRepository logRepository;

    @Mock
    private Model model;

    @BeforeEach
    public void before() {
        logRepository.clear();
    }

    @Test
    public void whenAddUserThenLog() {
        userController.addUser(model);
        List<Log> logs = logRepository.findAll();

        Assertions.assertNotNull(logs);
        Assertions.assertEquals(2, logs.size());
        Assertions.assertEquals(CINEMA12000.toString(), logs.get(0).getMessage());
        Assertions.assertEquals(CINEMA12001.toString(), logs.get(1).getMessage());
    }
}