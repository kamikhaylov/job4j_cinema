package ru.job4j.cinema.common.monitoring;

import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_ADD_TICKET_DURATION;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_ADD_TICKET_ERROR;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_ADD_TICKET_REQUEST;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_ADD_TICKET_SUCCESS;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_ADD_USER_DURATION;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_ADD_USER_ERROR;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_ADD_USER_REQUEST;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_ADD_USER_SUCCESS;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_CREATE_SESSION_DURATION;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_CREATE_SESSION_ERROR;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_CREATE_SESSION_REQUEST;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_CREATE_SESSION_SUCCESS;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_FIND_ALL_SESSIONS_DURATION;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_FIND_ALL_SESSIONS_ERROR;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_FIND_ALL_SESSIONS_REQUEST;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_FIND_ALL_SESSIONS_SUCCESS;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_GET_CELLS_DURATION;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_GET_CELLS_ERROR;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_GET_CELLS_REQUEST;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_GET_CELLS_SUCCESS;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_GET_ROWS_DURATION;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_GET_ROWS_ERROR;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_GET_ROWS_REQUEST;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_GET_ROWS_SUCCESS;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_LOGIN_DURATION;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_LOGIN_ERROR;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_LOGIN_REQUEST;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_LOGIN_SUCCESS;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_USER_REGISTRATION_DURATION;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_USER_REGISTRATION_ERROR;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_USER_REGISTRATION_REQUEST;
import static ru.job4j.cinema.common.monitoring.MonitoringMetrics.CINEMA_USER_REGISTRATION_SUCCESS;

/**
 * Точки мониторинга
 */
public enum CinemaMonitoringPoint implements MonitoringPoint {
    CINEMA_FIND_ALL_SESSIONS(
            CINEMA_FIND_ALL_SESSIONS_REQUEST,
            CINEMA_FIND_ALL_SESSIONS_DURATION,
            CINEMA_FIND_ALL_SESSIONS_SUCCESS,
            CINEMA_FIND_ALL_SESSIONS_ERROR),
    CINEMA_CREATE_SESSION(
            CINEMA_CREATE_SESSION_REQUEST,
            CINEMA_CREATE_SESSION_DURATION,
            CINEMA_CREATE_SESSION_SUCCESS,
            CINEMA_CREATE_SESSION_ERROR),
    CINEMA_GET_ROWS(
            CINEMA_GET_ROWS_REQUEST,
            CINEMA_GET_ROWS_DURATION,
            CINEMA_GET_ROWS_SUCCESS,
            CINEMA_GET_ROWS_ERROR),
    CINEMA_GET_CELLS(
            CINEMA_GET_CELLS_REQUEST,
            CINEMA_GET_CELLS_DURATION,
            CINEMA_GET_CELLS_SUCCESS,
            CINEMA_GET_CELLS_ERROR),
    CINEMA_ADD_TICKET(
            CINEMA_ADD_TICKET_REQUEST,
            CINEMA_ADD_TICKET_DURATION,
            CINEMA_ADD_TICKET_SUCCESS,
            CINEMA_ADD_TICKET_ERROR),
    CINEMA_USER_REGISTRATION(
            CINEMA_USER_REGISTRATION_REQUEST,
            CINEMA_USER_REGISTRATION_DURATION,
            CINEMA_USER_REGISTRATION_SUCCESS,
            CINEMA_USER_REGISTRATION_ERROR),
    CINEMA_LOGIN(
            CINEMA_LOGIN_REQUEST,
            CINEMA_LOGIN_DURATION,
            CINEMA_LOGIN_SUCCESS,
            CINEMA_LOGIN_ERROR),
    CINEMA_ADD_USER(
            CINEMA_ADD_USER_REQUEST,
            CINEMA_ADD_USER_DURATION,
            CINEMA_ADD_USER_SUCCESS,
            CINEMA_ADD_USER_ERROR);

    private final Metrics startEvent;
    private final Metrics durationEvent;
    private final Metrics successEvent;
    private final Metrics errorEvent;

    CinemaMonitoringPoint(Metrics startEvent, Metrics durationEvent, Metrics successEvent,
                          Metrics errorEvent) {
        this.startEvent = startEvent;
        this.durationEvent = durationEvent;
        this.successEvent = successEvent;
        this.errorEvent = errorEvent;
    }

    @Override
    public Metrics getStartEvent() {
        return startEvent;
    }

    @Override
    public Metrics getDurationEvent() {
        return durationEvent;
    }

    @Override
    public Metrics getSuccessEvent() {
        return successEvent;
    }

    @Override
    public Metrics getErrorEvent() {
        return errorEvent;
    }
}
