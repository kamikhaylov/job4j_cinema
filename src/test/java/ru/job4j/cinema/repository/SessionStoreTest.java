package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

/**
 * Тестрованиие работы с БД сеансов кинотеатра
 */
class SessionStoreTest {
    private static final String POSTER_PATH = "./src/test/resources/image/image.png";

    private static BasicDataSource pool;

    @BeforeEach
    public void before() {
        pool = new Main().loadPool();
    }

    @AfterEach
    public void after() throws SQLException {
        try (PreparedStatement st = pool.getConnection().prepareStatement(
                "DELETE FROM sessions;"
                        + "ALTER TABLE sessions ALTER COLUMN id RESTART WITH 1;")) {
            st.execute();
        }
    }

    private static Stream<Arguments> sessionProvider() throws IOException {
        return Stream.of(
                Arguments.of(new Session(1, "Film_01", createPoster(POSTER_PATH))),
                Arguments.of(new Session(2, "Film_02", createPoster(POSTER_PATH)))
        );
    }

    @ParameterizedTest
    @MethodSource("sessionProvider")
    public void whenCreateSession(Session session) {
        SessionStore store = new SessionStore(pool);

        boolean addResult = store.add(session);
        Session sessionInDb = store.findById(session.getId());
        List<Session> possessions = store.findAll();

        Assertions.assertTrue(addResult);
        Assertions.assertNotNull(sessionInDb);
        Assertions.assertEquals(possessions.size(), 1);
        Assertions.assertEquals(sessionInDb.getId(), session.getId());
        Assertions.assertEquals(sessionInDb.getName(), session.getName());
        Assertions.assertArrayEquals(sessionInDb.getPoster(), session.getPoster());
    }

    private static byte[] createPoster(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
}