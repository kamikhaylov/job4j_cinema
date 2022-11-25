package ru.job4j.cinema.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;
import org.springframework.ui.Model;
import ru.job4j.cinema.common.model.Session;
import ru.job4j.cinema.common.model.Ticket;
import ru.job4j.cinema.common.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестрованиие контроллера билетов
 */
class TicketControllerTest {
    private static final String POSTER_PATH = "./src/test/resources/image/image.png";
    private static final int SESSION_ID = 1;
    private static final String EMAIL = "test@test.ru";
    private static final String PHONE = "123";
    private static final String USER = "user";
    private static final List<Integer> ROWS = List.of(1, 2);
    private static final List<Integer> CELLS = List.of(1, 2);

    @Mock
    private Model model;
    @Mock
    private SessionService sessionService;
    @Mock
    private TicketService ticketService;
    @Mock
    private HttpSession httpSession;
    @Mock
    private HttpServletRequest req;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() {
        Mockito.reset(model, sessionService, ticketService, httpSession, req);
    }

    @Test
    public void whenFormRow() throws IOException {
        TicketController ticketController = new TicketController(sessionService, ticketService);
        Session session = new Session(SESSION_ID, "Film_01", createPoster(POSTER_PATH));
        User user = new User(0, USER, EMAIL, PHONE);
        when(httpSession.getAttribute(USER)).thenReturn(user);
        when(sessionService.findById(SESSION_ID)).thenReturn(session);
        when(ticketService.getRows(session)).thenReturn(ROWS);

        String page = ticketController.formRow(model, SESSION_ID, httpSession);

        verify(sessionService).findById(SESSION_ID);
        verify(model, times(2)).addAttribute(USER, user);
        verify(model).addAttribute("cinemaSession", session);
        verify(model).addAttribute("rows", ROWS);
        Assertions.assertEquals(page, "row");
    }

    @Test
    public void whenFormCell() throws IOException {
        TicketController ticketController = new TicketController(sessionService, ticketService);
        Session session = new Session(SESSION_ID, "Film_01", createPoster(POSTER_PATH));
        User user = new User(0, USER, EMAIL, PHONE);
        when(httpSession.getAttribute(USER)).thenReturn(user);
        when(httpSession.getAttribute("row")).thenReturn(1);
        when(httpSession.getAttribute("id")).thenReturn(SESSION_ID);
        when(sessionService.findById(SESSION_ID)).thenReturn(session);
        when(ticketService.getRows(session)).thenReturn(ROWS);
        when(ticketService.getCells(session, 1)).thenReturn(CELLS);

        String page = ticketController.formCell(model, 1, httpSession);

        verify(sessionService).findById(SESSION_ID);
        verify(model, times(2)).addAttribute(USER, user);
        verify(model).addAttribute("cinemaSession", session);
        verify(model).addAttribute("row", 1);
        verify(model).addAttribute("cells", CELLS);
        Assertions.assertEquals(page, "cell");
    }

    @Test
    public void whenCreateTicket() throws IOException {
        TicketController ticketController = new TicketController(sessionService, ticketService);
        Session session = new Session(SESSION_ID, "Film_01", createPoster(POSTER_PATH));
        User user = new User(0, USER, EMAIL, PHONE);
        Ticket ticket = new Ticket(1, 1, 1, 1, 0);
        when(httpSession.getAttribute(USER)).thenReturn(user);
        when(httpSession.getAttribute("row")).thenReturn(1);
        when(httpSession.getAttribute("id")).thenReturn(SESSION_ID);
        when(sessionService.findById(SESSION_ID)).thenReturn(session);
        when(ticketService.createTicket(session, 1, 1, user)).thenReturn(Optional.of(ticket));

        String page = ticketController.createTicket(model, 1, httpSession);

        verify(sessionService).findById(SESSION_ID);
        verify(model, times(2)).addAttribute(USER, user);
        verify(model).addAttribute("cinemaSession", session);
        verify(model).addAttribute("row", 1);
        verify(model).addAttribute("cell", 1);
        verify(model).addAttribute("ticketId", 1);
        Assertions.assertEquals(page, "ticketSuccess");
    }

    @Test
    public void whenCreateFail() throws IOException {
        TicketController ticketController = new TicketController(sessionService, ticketService);
        Session session = new Session(SESSION_ID, "Film_01", createPoster(POSTER_PATH));
        User user = new User(0, USER, EMAIL, PHONE);
        when(httpSession.getAttribute(USER)).thenReturn(user);
        when(httpSession.getAttribute("row")).thenReturn(1);
        when(httpSession.getAttribute("id")).thenReturn(SESSION_ID);
        when(sessionService.findById(SESSION_ID)).thenReturn(session);
        when(ticketService.createTicket(session, 1, 1, user)).thenReturn(Optional.empty());

        String page = ticketController.createTicket(model, 1, httpSession);

        verify(sessionService).findById(SESSION_ID);
        verify(model, times(2)).addAttribute(USER, user);
        verify(model).addAttribute("cinemaSession", session);
        verify(model).addAttribute("row", 1);
        verify(model).addAttribute("cell", 1);
        Assertions.assertEquals(page, "ticketFail");
    }

    private static byte[] createPoster(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
}