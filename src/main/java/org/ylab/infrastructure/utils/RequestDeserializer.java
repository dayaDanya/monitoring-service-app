package org.ylab.infrastructure.utils;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Класс предоставляющий метод десериализации входящего потока
 */
public class RequestDeserializer {
    /**
     * Конструктор по-умолчанию
     */
    public RequestDeserializer() {
    }

    /**
     *
     * @param reader получаемый из HttpServletRequest
     * @return строку с json
     * @throws IOException в случае ошибки чтения
     */
    public  String deserialize(BufferedReader reader) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        return jsonBuilder.toString();
    }
}
