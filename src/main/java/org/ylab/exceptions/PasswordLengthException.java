package org.ylab.exceptions;

/**
 *  Исключение сообщающее о неккоректной длине пароля
 */
public class PasswordLengthException extends RuntimeException {
    public PasswordLengthException() {
        super("Password length must be at least 8 symbols length");
    }
}
