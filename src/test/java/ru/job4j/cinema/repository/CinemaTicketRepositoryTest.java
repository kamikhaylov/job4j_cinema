package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.config.DataSourceConfigH2;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Тестрованиие работы с БД билетов
 */
class CinemaTicketRepositoryTest {
    private static final String POSTER_PATH = "./src/test/resources/image/image.png";

    private static DataSource dataSource;

    @BeforeEach
    public void before() {
        dataSource = new DataSourceConfigH2().loadDataSource();
    }

    @AfterEach
    public void after() throws SQLException {
        try (PreparedStatement st = dataSource.getConnection().prepareStatement(
                "DELETE FROM tickets;"
                        + "ALTER TABLE tickets ALTER COLUMN id RESTART WITH 1;"
                        + "DELETE FROM sessions;"
                        + "ALTER TABLE sessions ALTER COLUMN id RESTART WITH 1;"
                        + "DELETE FROM users;"
                        + "ALTER TABLE users ALTER COLUMN id RESTART WITH 1;")) {
            st.execute();
        }
    }

    @Test()
    public void whenTicket() throws IOException {
        CinemaSessionRepository sessionRepository = new CinemaSessionRepository(dataSource);
        CinemaUserRepository userRepository = new CinemaUserRepository(dataSource);
        CinemaTicketRepository ticketRepository = new CinemaTicketRepository(dataSource);

        Session session = new Session(1, "Film", createPoster(POSTER_PATH));
        User user = new User(1, "user", "test@test.ru", "123");

        userRepository.add(user);
        sessionRepository.add(session);
        Optional<Ticket> ticket = ticketRepository.add(session, 1, 1, user);

        Assertions.assertNotNull(ticket);
        Assertions.assertEquals(ticket.get().getId(), 1);
        Assertions.assertEquals(ticket.get().getPosRow(), 1);
        Assertions.assertEquals(ticket.get().getCell(), 1);
        Assertions.assertEquals(ticket.get().getUserId(), 1);
    }

    @Test()
    public void whenBusyCells() throws IOException {
        CinemaSessionRepository sessionRepository = new CinemaSessionRepository(dataSource);
        CinemaUserRepository userRepository = new CinemaUserRepository(dataSource);
        CinemaTicketRepository ticketRepository = new CinemaTicketRepository(dataSource);

        Session session = new Session(1, "Film", createPoster(POSTER_PATH));
        User user = new User(1, "user", "test@test.ru", "123");

        userRepository.add(user);
        sessionRepository.add(session);
        ticketRepository.add(session, 1, 1, user);
        ticketRepository.add(session, 1, 2, user);

        List<Integer> result = ticketRepository.getBusyCells(session, 1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result, new ArrayList<>(List.of(1, 2)));
    }

    @Test()
    public void whenBusyRows() throws IOException {
        CinemaSessionRepository sessionRepository = new CinemaSessionRepository(dataSource);
        CinemaUserRepository userRepository = new CinemaUserRepository(dataSource);
        CinemaTicketRepository ticketRepository = new CinemaTicketRepository(dataSource);

        Session session = new Session(1, "Film", createPoster(POSTER_PATH));
        User user = new User(1, "user", "test@test.ru", "123");

        userRepository.add(user);
        sessionRepository.add(session);
        ticketRepository.add(session, 1, 1, user);
        ticketRepository.add(session, 1, 2, user);

        List<Integer> result = ticketRepository.getBusyRows(session, 2);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result, new ArrayList<>(List.of(1)));
    }

    private static byte[] createPoster(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
}