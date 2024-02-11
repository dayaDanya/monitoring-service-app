package org.ylab.domain.dto;

/**
 * DTO - для возвращения ошибки при выбрасывании исключения или данных не имеющих типа.
 */
public class Response {
    private String message;

    /**
     * Конструктор
     *
     * @param message
     */
    public Response(String message) {
        this.message = message;
    }

    /**
     *  Геттер
     * @return сообщение
     */
    public String getMessage() {
        return message;
    }

    /**
     * сеттер
     * @param message сообщение
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
