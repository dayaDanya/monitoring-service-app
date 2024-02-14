package org.ylab.domain.dto;

import org.ylab.annotations.Default;
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
    @Default
    public CounterDto(long personId, CounterType counterType) {
        this.personId = personId;
        this.counterType = new CounterTypeDto(counterType.getName());
    }

    public CounterDto() {
    }

    //todo javadoc
    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public CounterTypeDto getCounterType() {
        return counterType;
    }

    public void setCounterType(CounterTypeDto counterType) {
        this.counterType = counterType;
    }
}
