package ru.job4j.cinema.common.identification;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация аспекта идентификации ролей пользователей
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RoleIdentified {

    /** Требуемая роль */
    Roles role();
}
