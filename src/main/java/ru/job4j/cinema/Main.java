package ru.job4j.cinema;

import net.jcip.annotations.ThreadSafe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

/**
 * Запуск приложения
 */
@ThreadSafe
@SpringBootApplication
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        LOGGER.info("Go to http://localhost:8080/sessions");
    }
}