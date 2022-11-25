package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.common.model.Session;
import ru.job4j.cinema.common.model.Ticket;
import ru.job4j.cinema.common.model.User;

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
class TicketStoreTest {
    private static final String POSTER_PATH = "./src/test/resources/image/image.png";

    private static BasicDataSource pool;

    @BeforeEach
    public void before() {
        pool = new Main().loadPool();
    }

    @AfterEach
    public void after() throws SQLException {
        try (PreparedStatement st = pool.getConnection().prepareStatement(
                "DELETE FROM ticket;"
                        + "ALTER TABLE ticket ALTER COLUMN id RESTART WITH 1;"
                        + "DELETE FROM sessions;"
                        + "ALTER TABLE sessions ALTER COLUMN id RESTART WITH 1;"
                        + "DELETE FROM users;"
                        + "ALTER TABLE users ALTER COLUMN id RESTART WITH 1;")) {
            st.execute();
        }
    }

    @Test()
    public void whenTicket() throws IOException {
        SessionStore sessionStore = new SessionStore(pool);
        UserStore userStore = new UserStore(pool);
        TicketStore ticketStore = new TicketStore(pool);

        Session session = new Session(1, "Film", createPoster(POSTER_PATH));
        User user = new User(1, "user", "test@test.ru", "123");

        userStore.add(user);
        sessionStore.add(session);
        Optional<Ticket> ticket = ticketStore.add(session, 1, 1, user);

        Assertions.assertNotNull(ticket);
        Assertions.assertEquals(ticket.get().getId(), 1);
        Assertions.assertEquals(ticket.get().getPosRow(), 1);
        Assertions.assertEquals(ticket.get().getCell(), 1);
        Assertions.assertEquals(ticket.get().getUserId(), 1);
    }

    @Test()
    public void whenBusyCells() throws IOException {
        SessionStore sessionStore = new SessionStore(pool);
        UserStore userStore = new UserStore(pool);
        TicketStore ticketStore = new TicketStore(pool);

        Session session = new Session(1, "Film", createPoster(POSTER_PATH));
        User user = new User(1, "user", "test@test.ru", "123");

        userStore.add(user);
        sessionStore.add(session);
        ticketStore.add(session, 1, 1, user);
        ticketStore.add(session, 1, 2, user);

        List<Integer> result = ticketStore.getBusyCells(session, 1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result, new ArrayList<>(List.of(1, 2)));
    }

    @Test()
    public void whenBusyRows() throws IOException {
        SessionStore sessionStore = new SessionStore(pool);
        UserStore userStore = new UserStore(pool);
        TicketStore ticketStore = new TicketStore(pool);

        Session session = new Session(1, "Film", createPoster(POSTER_PATH));
        User user = new User(1, "user", "test@test.ru", "123");

        userStore.add(user);
        sessionStore.add(session);
        ticketStore.add(session, 1, 1, user);
        ticketStore.add(session, 1, 2, user);

        List<Integer> result = ticketStore.getBusyRows(session, 2);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result, new ArrayList<>(List.of(1)));
    }

    private static byte[] createPoster(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
}