package org.ylab.infrastructure.in.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesInitializer {

    public static Properties initialize() {
        Properties properties = new Properties();

        try (InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("application.properties");) {

            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }
}
