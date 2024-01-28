package org.ylab.util;

/**
 * Исключение, сигнализирующее об ошибке при отсутствии счетчика.
 * Возникает, когда попытка получить или обработать несуществующий счетчик.
 */
public class CounterNotFoundException extends RuntimeException {

    /**
     * Конструктор исключения с сообщением по умолчанию "Counter not found!"
     */
    public CounterNotFoundException() {
        super("Counter not found!");
    }
}
