package ru.job4j.cinema.common.validation;

/**
 * Валидации входных параметров
 */
public enum Validators {
    CHECK_PHONES("^[0-9]{11}");

    private final String validator;

    Validators(String validator) {
        this.validator = validator;
    }

    public String getValidator() {
        return validator;
    }
}
