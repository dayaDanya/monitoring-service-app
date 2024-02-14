package org.ylab.exceptions;
/**
 * Исключение, сигнализирующее о неправильности переданного email
 */
public class EmailFormatException extends RuntimeException{
    public EmailFormatException(String email) {
        super("This does not looks like email: " + email);
    }
}
