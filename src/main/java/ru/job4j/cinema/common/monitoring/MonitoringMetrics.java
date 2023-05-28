package ru.job4j.cinema.common.monitoring;

/**
 * Метрики мониторинга
 */
public enum MonitoringMetrics implements Metrics {
    CINEMA_FIND_ALL_SESSIONS_REQUEST("Количество вызовов сервиса получения киносеансов"),
    CINEMA_FIND_ALL_SESSIONS_DURATION("Длительность ответа сервиса получения киносеансов"),
    CINEMA_FIND_ALL_SESSIONS_SUCCESS("Количество успешных вызовов сервиса получения киносеансов"),
    CINEMA_FIND_ALL_SESSIONS_ERROR("Количество ошибок при работе сервиса получения киносеансов"),

    CINEMA_CREATE_SESSION_REQUEST("Количество вызовов сервиса добавления киносеанса"),
    CINEMA_CREATE_SESSION_DURATION("Длительность ответа сервиса добавления киносеанса"),
    CINEMA_CREATE_SESSION_SUCCESS("Количество успешных вызовов сервиса добавления киносеанса"),
    CINEMA_CREATE_SESSION_ERROR("Количество ошибок при работе сервиса добавления киносеанса"),

    CINEMA_GET_ROWS_REQUEST("Количество вызовов сервиса выбора ряда в зале"),
    CINEMA_GET_ROWS_DURATION("Длительность ответа сервиса выбора ряда в зале"),
    CINEMA_GET_ROWS_SUCCESS("Количество успешных вызовов сервиса выбора ряда в зале"),
    CINEMA_GET_ROWS_ERROR("Количество ошибок при работе сервиса выбора ряда в зале"),

    CINEMA_GET_CELLS_REQUEST("Количество вызовов сервиса выбора места в ряду зала"),
    CINEMA_GET_CELLS_DURATION("Длительность ответа сервиса выбора места в ряду зала"),
    CINEMA_GET_CELLS_SUCCESS("Количество успешных вызовов сервиса выбора места в ряду зала"),
    CINEMA_GET_CELLS_ERROR("Количество ошибок при работе сервиса выбора места в ряду зала"),

    CINEMA_ADD_TICKET_REQUEST("Количество вызовов сервиса добавления билета киносеанса"),
    CINEMA_ADD_TICKET_DURATION("Длительность ответа сервиса добавления билета киносеанса"),
    CINEMA_ADD_TICKET_SUCCESS("Количество успешных вызовов сервиса добавления билета киносеанса"),
    CINEMA_ADD_TICKET_ERROR("Количество ошибок при работе сервиса добавления билета киносеанса"),

    CINEMA_USER_REGISTRATION_REQUEST("Количество вызовов сервиса регистрации пользователя"),
    CINEMA_USER_REGISTRATION_DURATION("Длительность ответа сервиса регистрации пользователя"),
    CINEMA_USER_REGISTRATION_SUCCESS("Количество успешных вызовов сервиса регистрации "
            + "пользователя"),
    CINEMA_USER_REGISTRATION_ERROR("Количество ошибок при работе сервиса регистрации пользователя"),

    CINEMA_LOGIN_REQUEST("Количество вызовов сервиса авторизации"),
    CINEMA_LOGIN_DURATION("Длительность ответа сервиса авторизации"),
    CINEMA_LOGIN_SUCCESS("Количество успешных вызовов сервиса авторизации"),
    CINEMA_LOGIN_ERROR("Количество ошибок при работе сервиса авторизации"),

    CINEMA_ADD_USER_REQUEST("Количество вызовов страницы регистрации пользователя"),
    CINEMA_ADD_USER_DURATION("Длительность ответа вызова страницы регистрации пользователя"),
    CINEMA_ADD_USER_SUCCESS("Количество успешных вызовов страницы регистрации пользователя"),
    CINEMA_ADD_USER_ERROR("Количество ошибок при вызове страницы регистрации пользователя");

    private final String title;

    MonitoringMetrics(String title) {
        this.title = title;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return getCode() + "." + getTitle();
    }
}
