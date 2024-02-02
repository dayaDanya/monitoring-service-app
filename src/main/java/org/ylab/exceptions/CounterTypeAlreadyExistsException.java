package org.ylab.exceptions;

public class CounterTypeAlreadyExistsException extends RuntimeException {
    public CounterTypeAlreadyExistsException(String message) {
        super("Counter type: " + message + " already exists");
    }
}
