package org.ylab.util;

public class BadMeasurementAmountException extends RuntimeException{
    public BadMeasurementAmountException() {
        super("Current measurement amount equals or smaller than previous");
    }
}
