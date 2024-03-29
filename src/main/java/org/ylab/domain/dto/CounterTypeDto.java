package org.ylab.domain.dto;

/**
 * Data Transfer Object (DTO) - тип счетчика,
 * класс для передачи данных о типе счетчика между слоями приложения
 */
public class CounterTypeDto {

    /**
     * Наименование типа счетчика
     */
    private String name;

    /**
     * Конструктор типа счетчика на основе наименования
     * @param name Наименование типа счетчика
     */
    public CounterTypeDto(String name) {
        this.name = name;
    }

    public CounterTypeDto() {
    }

    /**
     * Геттер для наименования типа счетчика
     * @return Наименование типа счетчика
     */
    public String getName() {
        return name;
    }

    /**
     * Сеттер для наименования типа счетчика
     * @param name Новое наименование типа счетчика
     */
    public void setName(String name) {
        this.name = name;
    }


}
