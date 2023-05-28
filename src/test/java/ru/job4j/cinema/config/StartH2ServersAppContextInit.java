package ru.job4j.cinema.config;

import org.h2.tools.Server;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import java.sql.SQLException;

/**
 * Инициализация h2
 */
public class StartH2ServersAppContextInit
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private Server server;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        startServers("9090");
        applicationContext.addApplicationListener(
                (ApplicationListener<ContextClosedEvent>) event -> {
                    server.stop();
        });
    }

    private void startServers(String tcpPort) {
        try {
            server = Server.createTcpServer("-tcp", "-tcpAllowOthers",
                    "-tcpPort", tcpPort, "-ifNotExists");
            server.run();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
}
