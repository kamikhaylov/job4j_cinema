package ru.job4j.cinema.common.monitoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import ru.job4j.cinema.AppTest;
import ru.job4j.cinema.controller.UserController;
import ru.job4j.cinema.model.Metric;
import ru.job4j.cinema.repository.LogRepository;
import ru.job4j.cinema.repository.MonitoringRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.job4j.cinema.common.monitoring.CinemaMonitoringPoint.CINEMA_ADD_USER;

/**
 * Тестирование аспекта мониторинга
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppTest.class })
class CinemaMonitoringAspectTest {

    @Autowired
    private UserController userController;

    @Autowired
    private MonitoringRepository monitoringRepository;

    @Autowired
    private LogRepository logRepository;

    @Mock
    private Model model;

    @BeforeEach
    public void before() {
        monitoringRepository.clear();
        logRepository.clear();
        Mockito.reset(model);
    }

    @Test
    public void whenAddUserThenMetric() {
        userController.addUser(model);
        List<Metric> metrics = monitoringRepository.findAll();

        assertNotNull(metrics);
        assertEquals(3, metrics.size());

        assertEquals(CINEMA_ADD_USER.getStartEvent().getCode(), metrics.get(0).getCode());
        assertEquals(CINEMA_ADD_USER.getStartEvent().getTitle(), metrics.get(0).getName());
        assertEquals(1.0D, metrics.get(0).getValue());
        assertNotNull(metrics.get(0).getCreated());

        assertEquals(CINEMA_ADD_USER.getDurationEvent().getCode(), metrics.get(1).getCode());
        assertEquals(CINEMA_ADD_USER.getDurationEvent().getTitle(), metrics.get(1).getName());
        assertTrue(metrics.get(1).getValue() >= 0D);
        assertNotNull(metrics.get(1).getCreated());

        assertEquals(CINEMA_ADD_USER.getSuccessEvent().getCode(), metrics.get(2).getCode());
        assertEquals(CINEMA_ADD_USER.getSuccessEvent().getTitle(), metrics.get(2).getName());
        assertEquals(1.0D, metrics.get(2).getValue());
        assertNotNull(metrics.get(2).getCreated());
    }
}