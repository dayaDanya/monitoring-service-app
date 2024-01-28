package org.ylab.util;

/**
 * Исключение, сигнализирующее об ошибке при отсутствии типа счетчика.
 * Возникает, когда попытка получить или обработать несуществующий тип счетчика.
 */
public class CounterTypeNotFoundException extends RuntimeException {

    /**
     * Конструктор исключения с сообщением по умолчанию "Counter type not found!"
     */
    public CounterTypeNotFoundException() {
        super("Counter type not found!");
    }
}
