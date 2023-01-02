package ru.job4j.cinema.config;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.cinema.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

/**
 * Подключение к H2 БД
 */
public class DataSourceConfigH2 {

    public BasicDataSource loadDataSource() {
        Properties cfg = loadDbProperties();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(cfg.getProperty("jdbc.driver"));
        dataSource.setUrl(cfg.getProperty("jdbc.url"));
        dataSource.setUsername(cfg.getProperty("jdbc.username"));
        dataSource.setPassword(cfg.getProperty("jdbc.password"));
        return dataSource;
    }

    private Properties loadDbProperties() {
        Properties cfg = load("db.properties");
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return cfg;
    }

    private Properties load(String fileName) {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(Main.class.getClassLoader()
                                .getResourceAsStream(fileName))
                )
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return cfg;
    }
}
