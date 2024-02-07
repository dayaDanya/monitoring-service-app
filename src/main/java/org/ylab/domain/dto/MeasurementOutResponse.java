package org.ylab.domain.dto;

import java.util.Map;

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
     * Map объектов MeasurementOutDto, представляющих собой информацию о измерениях
     */
    private Map<Long,MeasurementOutDto> dtoMap;

    /**
     * Конструктор ответа по измерениям
     * @param message Сообщение об успешном выполнении операции или информация об ошибке
     * @param dtoMap Список объектов MeasurementOutDto
     */
    public MeasurementOutResponse(String message, Map<Long,MeasurementOutDto> dtoMap) {
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
     * Геттер для получения map
     * @return Map
     */
    public Map<Long, MeasurementOutDto> getDtoMap() {
        return dtoMap;
    }

    /**
     * Метод для получения строкового представления списка измерений
     * @return Строковое представление списка измерений
     */
    public String printDtos() {
        StringBuilder builder = new StringBuilder();
        dtoMap.forEach((key, value) -> builder.append(value.toString()).append('\n'));
        return builder.toString();
    }
}
