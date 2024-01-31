package org.ylab.exceptions;

/**
 * Исключение, сигнализирующее об ошибке при отсутствии измерений.
 * Возникает, когда попытка получить или обработать отсутствующие измерения.
 */
public class MeasurementNotFoundException extends RuntimeException {

    /**
     * Конструктор исключения с сообщением по умолчанию "No measurements yet"
     */
    public MeasurementNotFoundException() {
        super("No measurements yet");
    }
}
