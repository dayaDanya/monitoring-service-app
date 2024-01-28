package org.ylab.util;

/**
 * Исключение, сигнализирующее об ошибке ввода неверных учетных данных.
 * Возникает, когда предоставленные учетные данные не соответствуют ожидаемым значениям.
 */
public class BadCredentialsException extends RuntimeException {

    /**
     * Конструктор исключения с сообщением по умолчанию "Bad credentials!"
     */
    public BadCredentialsException() {
        super("Bad credentials!");
    }
}
