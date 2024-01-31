package org.ylab.exceptions;

/**
 * Исключение, сигнализирующее об ошибке при отсутствии пользователя.
 * Возникает, когда попытка получить или обработать несуществующего пользователя.
 */
public class PersonNotFoundException extends RuntimeException {

    /**
     * Конструктор исключения с сообщением по умолчанию "Person not found!"
     */
    public PersonNotFoundException() {
        super("Person not found!");
    }
}
