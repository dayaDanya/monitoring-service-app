package org.ylab.domain.dto;

import org.ylab.annotations.Default;
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
     * Конструктор выходных данных для измерения
     *
     */
    @Default
    public MeasurementOutDto(long counterId, double amount,
                             String counterType, LocalDateTime submissionDate) {
        this.counterId = counterId;
        this.amount = amount;
        this.counterType = counterType;
        this.submissionDate = submissionDate;
    }
    //todo javadoc

    public long getCounterId() {
        return counterId;
    }

    public void setCounterId(long counterId) {
        this.counterId = counterId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCounterType() {
        return counterType;
    }

    public void setCounterType(String counterType) {
        this.counterType = counterType;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }
}
