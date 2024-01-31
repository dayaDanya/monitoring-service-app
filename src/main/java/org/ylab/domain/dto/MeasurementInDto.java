package org.ylab.domain.dto;

/**
 * Data Transfer Object (DTO) - входные данные для измерения,
 * класс для передачи данных о показании между слоями приложения
 */
public class MeasurementInDto {

    /**
     * Значение на счетчике
     */
    private double amount;

    /**
     * Тип счетчика
     */
    private String counterType;

    /**
     * Конструктор входных данных для измерения
     * @param amount Значение на счетчике
     * @param counterType Тип счетчика
     */
    public MeasurementInDto(double amount, String counterType) {
        this.amount = amount;
        this.counterType = counterType;
    }

    /**
     * Геттер для значения на счетчике
     * @return Значение на счетчике
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Геттер для типа счетчика
     * @return Тип счетчика
     */
    public String getCounterType() {
        return counterType;
    }
}
