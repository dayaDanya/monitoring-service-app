package org.ylab.exceptions;

/**
 * Класс выбрасывающий исключение в случае отсутствия Action
 */
public class ActionNotFoundException extends RuntimeException{
    /**
     * Конструктор с сообщением "Action not found!"
     */
    public ActionNotFoundException() {
        super("Action not found!");
    }
}
