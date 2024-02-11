package org.ylab.exceptions;

public class PasswordLengthException extends RuntimeException {
    public PasswordLengthException() {
        super("Password length must be at least 8 symbols length");
    }
}
