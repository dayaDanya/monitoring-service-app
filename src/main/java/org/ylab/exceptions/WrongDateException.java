package org.ylab.exceptions;

import java.time.LocalDateTime;

/**
 * Исключение, сигнализирующее об ошибке при попытке сохранить измерение в недопустимую дату.
 * Возникает, когда дата сохранения измерения не соответствует логике
 */
public class WrongDateException extends RuntimeException {

    /**
     * Конструктор исключения с сообщением, содержащим информацию о неверной дате.
     * @param lastMeasurementDate Последняя дата измерения, используемая для расчета следующей возможной даты сохранения
     */
    public WrongDateException(LocalDateTime lastMeasurementDate) {
        super("Can't save measurement now. The next possible save date: " + lastMeasurementDate.plusDays(30));
    }
}
