package org.ylab.domain.models;

import org.ylab.domain.models.enums.Action;

import java.time.LocalDateTime;

/**
 * Сущность - операция,
 * класс используемый для аудита
 */
public class Operation {

    /**
     * идентификатор операции
     */
    private long id;

    /**
     * идентификатор пользователя, который выполняет операцию
     */
    private long personId;

    /**
     * Действие, совершенное в рамках операции
     */
    private Action action;

    /**
     * Дата выполнения операции
     */
    private LocalDateTime date;

    /**
     * Конструктор операции
     * @param personId Идентификатор пользователя
     * @param action Действие, совершенное в рамках операции
     * @param date Дата выполнения операции
     */
    public Operation(long personId, Action action, LocalDateTime date) {
        this.personId = personId;
        this.action = action;
        this.date = date;
    }

    /**
     * Геттер для уникального идентификатора операции
     * @return Уникальный идентификатор операции
     */
    public long getId() {
        return id;
    }

    /**
     * Сеттер для уникального идентификатора операции
     * @param id Уникальный идентификатор операции
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Геттер для идентификатора пользователя
     * @return Идентификатор пользователя
     */
    public long getPersonId() {
        return personId;
    }

    /**
     * Геттер для действия, совершенного в рамках операции
     * @return Действие, совершенное в рамках операции
     */
    public Action getAction() {
        return action;
    }

    /**
     * Геттер для даты выполнения операции
     * @return Дата выполнения операции
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Переопределение метода toString для удобного вывода информации об операции
     * @return Строковое представление объекта Operation
     */
    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", personId=" + personId +
                ", action=" + action +
                ", date=" + date +
                '}';
    }
}

