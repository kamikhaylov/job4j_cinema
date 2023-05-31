package ru.job4j.cinema.common.tracing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.job4j.cinema.AppTest;
import ru.job4j.cinema.controller.SessionController;
import ru.job4j.cinema.model.Log;
import ru.job4j.cinema.repository.LogRepository;
import ru.job4j.cinema.service.SessionService;

import java.util.List;

import static org.mockito.Mockito.when;
import static ru.job4j.cinema.common.logging.CinemaLogEvent.CINEMA10003;

/**
 * Тестирование аспекта обработки исключений.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppTest.class })
class CinemaTracingAspectTest {

    @Autowired
    private SessionController sessionController;

    @Autowired
    private LogRepository logRepository;

    @Mock
    private SessionService sessionService;

    @BeforeEach
    public void before() {
        logRepository.clear();
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() {
        Mockito.reset(sessionService);
    }

    @Test
    public void whenGetPosterThenException()  {
        when(sessionService.findById(1)).thenThrow(NullPointerException.class);

        try {
            sessionController.download(1);
            Assertions.fail();
        } catch (NullPointerException e) {
            List<Log> logs = logRepository.findAll();
            Assertions.assertNotNull(logs);
            Assertions.assertEquals(1, logs.size());
            Assertions.assertEquals(CINEMA10003.toString(), logs.get(0).getMessage());
            Assertions.assertNotNull(logs.get(0).getTrace());
        }
    }
}