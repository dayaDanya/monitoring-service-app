package org.ylab.domain.dto;

import java.util.List;

/**
 * Класс - ответ на запрос по измерениям,
 * предоставляет информацию о выполнении операции и списке измерений
 */
public class MeasurementOutResponse {

    /**
     * Сообщение об успешном выполнении операции или информация об ошибке
     */
    private String message;

    /**
     * Список объектов MeasurementOutDto, представляющих собой информацию о измерениях
     */
    private List<MeasurementOutDto> dtoList;

    /**
     * Конструктор ответа по измерениям
     * @param message Сообщение об успешном выполнении операции или информация об ошибке
     * @param dtoList Список объектов MeasurementOutDto
     */
    public MeasurementOutResponse(String message, List<MeasurementOutDto> dtoList) {
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
     * Геттер для списка объектов MeasurementOutDto
     * @return Список объектов MeasurementOutDto
     */
    public List<MeasurementOutDto> getDtoList() {
        return dtoList;
    }

    /**
     * Метод для получения строкового представления списка измерений
     * @return Строковое представление списка измерений
     */
    public String printDtos() {
        StringBuilder builder = new StringBuilder();
        dtoList.forEach(d -> builder.append(d.toString()).append('\n'));
        return builder.toString();
    }
}
