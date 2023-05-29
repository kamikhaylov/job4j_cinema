package ru.job4j.cinema.common.identification;

/**
 * Роли.
 */
public enum Roles {
    USER("Базовая роль"),
    ADMIN("Административная роль");

    private final String title;

    Roles(String title) {
        this.title = title;
    }

    public String getCode() {
        return name();
    }

    public String getTitle() {
        return title;
    }
}
