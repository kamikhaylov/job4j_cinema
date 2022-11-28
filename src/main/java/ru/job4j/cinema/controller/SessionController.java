package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.common.util.UserSession;
import ru.job4j.cinema.service.SessionService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Контроллер сеансов кинотеатра
 */
@ThreadSafe
@Controller
public class SessionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionController.class.getName());

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Страница сеансов кинотеатра
     * @param model - модель данных
     * @param httpSession - пользовательская сессия
     * @return возвращает страницу сеансов кинотеатра
     */
    @GetMapping("/sessions")
    public String sessions(Model model, HttpSession httpSession) {
        LOGGER.info("SessionController.sessions");

        User user = UserSession.getUser(model, httpSession);
        List<Session> list = (List<Session>) sessionService.findAll();
        for (Session session : list) {
            LOGGER.info(session.toString());
        }
        model.addAttribute("sessions", list);
        model.addAttribute("user", user);
        return "sessions";
    }

    /**
     * Страница добавления сеансов кинотеатра
     * @param model - модель данных
     * @param httpSession - сеанс кинотеатра
     * @return возвращает страницу сеансов кинотеатра
     */
    @GetMapping("/formAddSession")
    public String addSession(Model model, HttpSession httpSession) {
        LOGGER.info("SessionController.addSession");

        User user = UserSession.getUser(model, httpSession);
        model.addAttribute("session", new Session(0, null, null));
        model.addAttribute("user", user);
        return "addSession";
    }

    /**
     * Добавление сеанса кинотеатра
     * @param session - сеанс кинотеатра
     * @return возвращает страницу сеансов кинотеатра
     */
    @PostMapping("/createSession")
    public String createSession(@ModelAttribute Session session,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        LOGGER.info("SessionController.createSession : name : " + session.getName()
                + ", file.name : "  + file.getName());

        session.setPoster(file.getBytes());
        sessionService.add(session);
        return "redirect:/sessions";
    }

    /**
     * Отображение постера
     * @param id - идентификатор сеанса кинотеатра
     * @return возвращает постер
     */
    @GetMapping("/poster/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") Integer id) {
        LOGGER.info("SessionController.download : " + id);

        Session session = sessionService.findById(id);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(session.getPoster().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(session.getPoster()));
    }
}