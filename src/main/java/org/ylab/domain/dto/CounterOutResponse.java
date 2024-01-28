package org.ylab.domain.dto;

import java.util.List;

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
    private List<CounterDto> dtoList;

    /**
     * Конструктор ответа по счетчикам
     * @param message Сообщение об успешном выполнении операции или информация об ошибке
     * @param dtoList Список объектов CounterDto
     */
    public CounterOutResponse(String message, List<CounterDto> dtoList) {
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
     * Геттер для списка объектов CounterDto
     * @return Список объектов CounterDto
     */
    public List<CounterDto> getDtoList() {
        return dtoList;
    }

    /**
     * Метод для получения строкового представления списка счетчиков
     * @return Строковое представление списка счетчиков
     */
    public String printDtos() {
        StringBuilder builder = new StringBuilder();
        dtoList.forEach(d -> builder.append(d.toString()).append('\n'));
        return builder.toString();
    }
}
