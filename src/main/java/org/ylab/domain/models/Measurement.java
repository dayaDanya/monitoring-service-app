package org.ylab.domain.models;

import org.ylab.annotations.Default;
import org.ylab.domain.dto.MeasurementInDto;

import java.time.LocalDateTime;

/**
 * Сущность - измерение или показание,
 * класс описавающий показание подаваемое пользователем
 */
public class Measurement {
    /**
     * уникальный айди
     */
    private long id;
    /**
     * значение на счетчике
     */
    private double amount;

    /**
     * дата подачи
     */
    private LocalDateTime submissionDate;

    /**
     * тип счетчика
     */
    private String counterType;

    /**
     * айди счетчика
     */
    private long counterId;

    /**
     * конструктор
     * @param amount текущее значение
     * @param counterType тип счетчика
     */
    @Default
    public Measurement(double amount, String counterType) {
        this.amount = amount;
        this.counterType = counterType;
    }



    /**
     * конструктор
     * @param id идентификатор
     * @param counterId идентификатор счетчика
     * @param amount значение на счетчике
     * @param counterType значение на счетчике
     * @param submissionDate дата подачи
     */
    public Measurement(long id, double amount,
                       LocalDateTime submissionDate, String counterType, long counterId) {
        this.id = id;
        this.amount = amount;
        this.submissionDate = submissionDate;
        this.counterType = counterType;
        this.counterId = counterId;
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
     * @return значение на счетчике
     */
    public double getAmount() {
        return amount;
    }

    /**
     * геттер
     * @return дата подачи
     */
    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }
    /**
     * геттер
     * @param submissionDate дата подачи
     */
    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    /**
     * геттер
     * @return айди счетчика
     */
    public long getCounterId() {
        return counterId;
    }

    /**
     * геттер
     * @return тип счетчика
     */
    public String getCounterType() {
        return counterType;
    }

    /**
     * сеттер
     * @param counterId айди счетчика
     */
    public void setCounterId(long counterId) {
        this.counterId = counterId;
    }
}
