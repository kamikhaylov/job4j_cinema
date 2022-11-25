package ru.job4j.cinema.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.common.model.Session;
import ru.job4j.cinema.service.SessionService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестрованиие контроллера сеансов кинотеатра
 */
class SessionControllerTest {
    private static final String POSTER_PATH = "./src/test/resources/image/image.png";

    @Mock
    private Model model;
    @Mock
    private SessionService sessionService;
    @Mock
    private HttpSession httpSession;
    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() {
        Mockito.reset(model, sessionService, httpSession, multipartFile);
    }

    @Test
    public void whenSessions() throws IOException {
        List<Session> sessions = Arrays.asList(
                new Session(1, "Film_01", createPoster(POSTER_PATH)),
                new Session(1, "Film_02", createPoster(POSTER_PATH))
        );
        when(sessionService.findAll()).thenReturn(sessions);
        SessionController sessionController = new SessionController(sessionService);
        String page = sessionController.sessions(model, httpSession);

        verify(model).addAttribute("sessions", sessions);
        Assertions.assertEquals(page, "sessions");
    }

    @Test
    public void whenCreateSession() throws IOException {
        Session session = new Session(1, "Film_01", createPoster(POSTER_PATH));
        SessionController sessionController = new SessionController(sessionService);

        String page = sessionController.createSession(session, multipartFile);

        verify(sessionService).add(session);
        Assertions.assertEquals(page, "redirect:/sessions");
    }

    @Test
    public void whenDownload() throws IOException {
        byte[] poster = createPoster(POSTER_PATH);
        Session session = new Session(1, "Film_01", poster);
        SessionController sessionController = new SessionController(sessionService);
        when(sessionService.findById(1)).thenReturn(session);
        ResponseEntity<Resource> expected = ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(session.getPoster().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(session.getPoster()));

        ResponseEntity<Resource> actual = sessionController.download(1);

        verify(sessionService).findById(1);
        Assertions.assertEquals(actual, expected);
    }

    private static byte[] createPoster(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
}