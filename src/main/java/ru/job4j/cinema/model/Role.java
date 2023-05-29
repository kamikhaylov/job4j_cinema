package ru.job4j.cinema.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель роль пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    /** Идентификатор пользователя */
    private int id;
    /** Роль пользователя */
    private String name;
}
