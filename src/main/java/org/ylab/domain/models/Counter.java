package org.ylab.domain.models;

import java.util.List;

/**
 * Сущность - счетчик
 * Класс описывающий счетчик, имеющий поля
 * personId - id  человека обладающего счетчиком
 * measurements - список показаний
 * counterType - тип счетчика
 */
public class Counter {
    /**
     * уникальный айди
     */
    private long id;

    /**
     * айди пользователя
     */
    private long personId;

    /**
     * список показаний
     */
    private List<Measurement> measurements;

    /**
     * тип счетчика
     */
    private CounterType counterType;


    /**
     * конструктор
     * @param personId айди пользователя
     * @param counterType тип счетчика
     */
    public Counter(long personId, CounterType counterType) {
        this.personId = personId;
        this.counterType = counterType;
    }

    /**
     * геттер
     * @return id идентификатор счетчика
     */
    public long getId() {
        return id;
    }


    /**
     * геттер
     * @return personId айди пользователя
     */
    public long getPersonId() {
        return personId;
    }


    /**
     * геттер
     * @return тип счетчика
     */
    public CounterType getCounterType() {
        return counterType;
    }

    /**
     * сеттер
     * @param counterType тип счетчика
     */
    public void setCounterType(CounterType counterType) {
        this.counterType = counterType;
    }

    /**
     * сеттер
     * @param id устанавливает айди счетчика
     */
    public void setId(long id) {
        this.id = id;
    }




}
