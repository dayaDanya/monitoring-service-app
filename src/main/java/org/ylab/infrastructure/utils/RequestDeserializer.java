package org.ylab.infrastructure.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestDeserializer {
    public static String deserialize(BufferedReader reader) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        return jsonBuilder.toString();
    }
}
