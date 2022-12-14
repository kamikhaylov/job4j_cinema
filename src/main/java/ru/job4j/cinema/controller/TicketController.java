package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.common.util.UserSession;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Контроллер билетов
 */
@ThreadSafe
@Controller
public class TicketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionController.class.getName());

    private final SessionService sessionService;
    private final TicketService ticketService;

    public TicketController(SessionService sessionService, TicketService ticketService) {
        this.sessionService = sessionService;
        this.ticketService = ticketService;
    }

    /**
     * Выбор ряда в зале
     * @param id - идентификатор сеанса кинотеатра
     * @return возвращает страницу выбора ряд
     */
    @GetMapping("/row/{sessionId}")
    public String formRow(Model model, @PathVariable("sessionId") int id, HttpSession httpSession) {
        LOGGER.info("SessionController.formRow : " + id);

        User user = UserSession.getUser(model, httpSession);
        Session session = sessionService.findById(id);
        httpSession.setAttribute("id", id);
        model.addAttribute("user", user);
        model.addAttribute("cinemaSession", session);
        model.addAttribute("rows", ticketService.getRows(session));
        return "row";
    }

    /**
     * Выбор места в ряду зала
     * @param row - ряд
     * @return возвращает страницу выбора места
     */
    @GetMapping("/cell")
    public String formCell(Model model, @RequestParam("row.id") int row, HttpSession httpSession) {
        LOGGER.info("SessionController.formCell : " + row);

        User user = UserSession.getUser(model, httpSession);
        Session session = sessionService.findById((Integer) httpSession.getAttribute("id"));
        httpSession.setAttribute("row", row);
        model.addAttribute("user", user);
        model.addAttribute("cinemaSession", session);
        model.addAttribute("row", httpSession.getAttribute("row"));
        model.addAttribute("cells",
                ticketService.getCells(session, (Integer) httpSession.getAttribute("row")));
        return "cell";
    }

    /**
     * Добавление билета сеанса кинотеатра
     * @param cell - место
     * @return возвращает страницу с результатом операции
     */
    @PostMapping("/addTicket")
    public String createTicket(Model model, @RequestParam("cell.id") int cell,
                               HttpSession httpSession) {
        LOGGER.info("SessionController.addTicket : " + cell);

        User user = UserSession.getUser(model, httpSession);
        Session session = sessionService.findById((Integer) httpSession.getAttribute("id"));
        int row = (Integer) httpSession.getAttribute("row");
        model.addAttribute("user", user);
        model.addAttribute("cinemaSession", session);
        model.addAttribute("row", row);
        model.addAttribute("cell", cell);
        Optional<Ticket> ticket = ticketService.createTicket(session, row, cell, user);

        if (ticket.isEmpty()) {
            return "ticketFail";
        }

        model.addAttribute("ticketId", ticket.get().getId());
        return "ticketSuccess";
    }
}
