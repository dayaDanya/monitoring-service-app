package org.ylab.exceptions;

public class BadMeasurementAmountException extends RuntimeException{
    public BadMeasurementAmountException() {
        super("Current measurement amount equals or smaller than previous");
    }
}
