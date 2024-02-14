package org.ylab.domain.models;

import org.ylab.annotations.Default;

/**
 * Сущность - тип счетчика,
 * по заданию имеем три типа: HOT, COLD, HEAT
 */
public class CounterType {
    /**
     * уникальный айди
     */
    private long id;
    /**
     * название типа
     */
    private String name;

    /**
     * конструктор
     * @param name название типа
     */
    @Default
    public CounterType(String name) {
        this.name = name;
    }
    /**
     * конструктор
     * @param id идентификатор
     * @param name название типа
     */
    public CounterType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * геттер
     * @return уникальный айди
     */
    public long getId() {
        return id;
    }

    /**
     * сеттер
     * @param id уникальный айди
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * геттер
     * @return название типа
     */
    public String getName() {
        return name;
    }


}
