package org.ylab.domain.dto;

import org.ylab.annotations.Default;
import org.ylab.domain.models.Operation;
import org.ylab.domain.enums.Action;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) - выходные данные для операции,
 * класс для передачи данных о действии пользователя между слоями приложения
 */
public class OperationOutDto {

    /**
     * Идентификатор пользователя, над которым выполнена операция
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
     * Конструктор выходных данных для операции на основе идентификатора пользователя, действия и даты
     *
     * @param personId Идентификатор пользователя
     * @param action   Действие, совершенное в рамках операции
     * @param date     Дата выполнения операции
     */
    @Default
    public OperationOutDto(long personId, Action action, LocalDateTime date) {
        this.personId = personId;
        this.action = action;
        this.date = date;
    }

    //todo javadoc
    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
