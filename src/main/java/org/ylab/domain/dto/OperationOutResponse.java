package org.ylab.domain.dto;

import java.util.List;

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
     * Список объектов OperationOutDto, представляющих собой информацию о операциях
     */
    private List<OperationOutDto> dtoList;

    /**
     * Конструктор ответа по операциям
     * @param message Сообщение об успешном выполнении операции или информация об ошибке
     * @param dtoList Список объектов OperationOutDto
     */
    public OperationOutResponse(String message, List<OperationOutDto> dtoList) {
        this.message = message;
        this.dtoList = dtoList;
    }

    /**
     * Геттер для сообщения об успешном выполнении операции или информации об ошибке
     * @return Сообщение
     */
    public String getMessage() {
        return message;
    }

    /**
     * Геттер для списка объектов OperationOutDto
     * @return Список объектов OperationOutDto
     */
    public List<OperationOutDto> getDtoList() {
        return dtoList;
    }

    /**
     * Метод для получения строкового представления списка операций
     * @return Строковое представление списка операций
     */
    public String printDtos() {
        StringBuilder builder = new StringBuilder();
        dtoList.forEach(d -> builder.append(d.toString()).append('\n'));
        return builder.toString();
    }
}
