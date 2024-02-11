package org.ylab.exceptions;

/**
 * Исключение сигнализирующее о неправильном значении переданного измерения
 * Возникает когда вновь переданное значение меньше либо совпадает с предыдущим.
 */
public class BadMeasurementAmountException extends RuntimeException{
    public BadMeasurementAmountException() {
        super("Current measurement amount equals or smaller than previous");
    }
}
