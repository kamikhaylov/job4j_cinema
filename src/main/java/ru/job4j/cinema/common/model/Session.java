package ru.job4j.cinema.common.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Модель сеанса кинотеатра
 */
public class Session {
    /** Идентификатор сеанса */
    private int id;
    /** Названиие сеанса */
    private String name;
    /** Постер сеанса */
    private byte[] poster;

    public Session() {
    }

    public Session(String name, byte[] poster) {
        this.name = name;
        this.poster = poster;
    }

    public Session(int id, String name, byte[] poster) {
        this.id = id;
        this.name = name;
        this.poster = poster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return id == session.id
                && Objects.equals(name, session.name)
                && Arrays.equals(poster, session.poster);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name);
        result = 31 * result + Arrays.hashCode(poster);
        return result;
    }

    @Override
    public String toString() {
        return "Session: id=" + id
                + ", name=" + name;
    }
}