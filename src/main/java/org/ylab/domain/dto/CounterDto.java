package org.ylab.domain.dto;

import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;

/**
 * Data Transfer Object (DTO) - счетчик,
 * класс для передачи данных о счетчике между слоями приложения
 */
public class CounterDto {

    /**
     * Идентификатор пользователя, которому принадлежит счетчик
     */
    private long personId;

    /**
     * DTO для типа счетчика
     */
    private CounterTypeDto counterType;

    /**
     * Конструктор счетчика на основе идентификатора пользователя и типа счетчика
     * @param personId Идентификатор пользователя
     * @param counterType Тип счетчика
     */
    public CounterDto(long personId, CounterType counterType) {
        this.personId = personId;
        this.counterType = new CounterTypeDto(counterType.getName());
    }

    /**
     * Конструктор счетчика на основе объекта Counter
     * @param counter Объект счетчика
     */
    public CounterDto(Counter counter) {
        this.personId = counter.getPersonId();
        this.counterType = new CounterTypeDto(counter.getCounterType().getName());
    }

    /**
     * Переопределение метода toString для удобного вывода информации о счетчике
     * @return Строковое представление объекта CounterDto
     */
    @Override
    public String toString() {
        return "CounterDto{" +
                "personId=" + personId +
                ", counterType=" + counterType +
                '}';
    }
}
