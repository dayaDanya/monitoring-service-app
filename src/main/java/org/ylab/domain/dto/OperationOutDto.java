package org.ylab.domain.dto;

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
     * @param personId Идентификатор пользователя
     * @param action Действие, совершенное в рамках операции
     * @param date Дата выполнения операции
     */
    public OperationOutDto(long personId, Action action, LocalDateTime date) {
        this.personId = personId;
        this.action = action;
        this.date = date;
    }

    /**
     * Конструктор выходных данных для операции на основе объекта Operation
     * @param operation Объект Operation
     */
    public OperationOutDto(Operation operation) {
        this.personId = operation.getPersonId();
        this.action = operation.getAction();
        this.date = operation.getDate();
    }

    /**
     * Переопределение метода toString для удобного вывода информации о операции
     * @return Строковое представление объекта OperationOutDto
     */
    @Override
    public String toString() {
        return "OperationOutDto{" +
                "personId=" + personId +
                ", action=" + action +
                ", date=" + date +
                '}';
    }
}
