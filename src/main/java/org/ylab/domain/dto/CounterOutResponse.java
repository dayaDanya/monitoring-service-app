package org.ylab.domain.dto;

import java.util.Map;

/**
 * Класс - ответ на запрос по счетчикам,
 * предоставляет информацию о выполнении операции и списке счетчиков
 */
public class CounterOutResponse {

    /**
     * Сообщение об успешном выполнении операции или информация об ошибке
     */
    private String message;

    /**
     * Список объектов CounterDto, представляющих собой информацию о счетчиках
     */
    private Map<Long, CounterDto> dtoMap;

    /**
     * Конструктор ответа по счетчикам
     * @param message Сообщение об успешном выполнении операции или информация об ошибке
     * @param dtoMap Map объектов CounterDto
     */
    public CounterOutResponse(String message, Map<Long, CounterDto> dtoMap) {
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
     * Геттер для map
     * @return Map
     */
    public Map<Long, CounterDto> getDtoMap() {
        return dtoMap;
    }

    /**
     * Метод для получения строкового представления списка счетчиков
     * @return Строковое представление списка счетчиков
     */
    public String printDtos() {
        StringBuilder builder = new StringBuilder();
        dtoMap.forEach((key, value) -> builder.append(value.toString()).append('\n'));
        return builder.toString();
    }
}
