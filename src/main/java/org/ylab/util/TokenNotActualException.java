package org.ylab.util;

/**
 * Исключение, сигнализирующее об ошибке при использовании неактуального токена.
 * Возникает, когда попытка обработать или валидировать токен, который устарел или неактуален.
 */
public class TokenNotActualException extends RuntimeException {

    /**
     * Конструктор исключения с сообщением по умолчанию "Token is not actual"
     */
    public TokenNotActualException() {
        super("Token is not actual");
    }
}
