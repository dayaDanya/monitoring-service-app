package org.ylab.domain.dto;

import org.ylab.domain.models.Measurement;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) - выходные данные для измерения,
 * класс для передачи данных о показании между слоями приложения
 */
public class MeasurementOutDto {

    /**
     * Идентификатор счетчика
     */
    private long counterId;

    /**
     * Значение на счетчике
     */
    private double amount;

    /**
     * Тип счетчика
     */
    private String counterType;

    /**
     * Дата подачи измерения
     */
    private LocalDateTime submissionDate;

    /**
     * Конструктор выходных данных для измерения на основе объекта Measurement
     * @param measurement Объект Measurement
     */
    public MeasurementOutDto(Measurement measurement) {
        this.amount = measurement.getAmount();
        this.counterType = measurement.getCounterType();
        this.counterId = measurement.getCounterId();
        this.submissionDate = measurement.getSubmissionDate();
    }

    /**
     * Переопределение метода toString для удобного вывода информации о измерении
     * @return Строковое представление объекта MeasurementOutDto
     */
    @Override
    public String toString() {
        return "MeasurementOutDto{" +
                "counterId=" + counterId +
                ", amount=" + amount +
                ", counterType=" + counterType +
                ", submissionDate=" + submissionDate +
                '}';
    }
}
