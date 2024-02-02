package org.ylab.domain.dto;

import java.util.Map;

/**
 * Класс - ответ на запрос по операциям,
 * предоставляет информацию о выполнении операции и списке операций
 */
public class OperationOutResponse {

    /**
     * Сообщение об успешном выполнении операции или информация об ошибке
     */
    private String message;

    /**
     * Map объектов OperationOutDto, представляющих собой информацию о операциях
     */
    private Map<Long,OperationOutDto> dtoMap;

    /**
     * Конструктор ответа по операциям
     * @param message Сообщение об успешном выполнении операции или информация об ошибке
     * @param dtoMap Map объектов OperationOutDto
     */
    public OperationOutResponse(String message, Map<Long,OperationOutDto> dtoMap) {
        this.message = message;
        this.dtoMap = dtoMap;
    }

    /**
     * Геттер для сообщения об успешном выполнении операции или информации об ошибке
     * @return Сообщение
     */
    public String getMessage() {
        return message;
    }


    /**
     * Метод для получения строкового представления списка операций
     * @return Строковое представление map операций
     */
    public String printDtos() {
        StringBuilder builder = new StringBuilder();
        dtoMap.forEach((key, value) -> builder.append(value.toString()).append('\n'));
        return builder.toString();
    }
}
