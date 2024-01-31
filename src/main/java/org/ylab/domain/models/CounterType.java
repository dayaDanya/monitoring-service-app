package org.ylab.domain.models;

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
    public CounterType(String name) {
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
