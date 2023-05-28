package ru.job4j.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.job4j.cinema.config.StartH2ServersAppContextInit;

/**
 * Запуск приложения для тестов с h2
 */
@SpringBootApplication
public class AppTest {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AppTest.class);
        application.addInitializers(new StartH2ServersAppContextInit());
        application.run();
    }
}
