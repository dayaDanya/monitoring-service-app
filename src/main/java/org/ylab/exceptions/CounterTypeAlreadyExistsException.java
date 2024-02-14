package org.ylab.exceptions;

/**
 *  Исключение сигнализирующее о том, что тип счетчика уже существует
 */
public class CounterTypeAlreadyExistsException extends RuntimeException {
    public CounterTypeAlreadyExistsException(String message) {
        super("Counter type: " + message + " already exists");
    }
}
