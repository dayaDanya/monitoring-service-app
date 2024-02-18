package org.ylab.infrastructure.in.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Классом являющийся адаптером для подключения к бд
 */
public class ConnectionAdapter {
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;

    /**
     * Конструктор с параметрами
     * @param URL адрес БД
     * @param USER_NAME пользователь БД
     * @param PASSWORD пароль пользователя
     */
    public ConnectionAdapter(String URL, String USER_NAME, String PASSWORD) {
        this.URL = URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }

    /**
     * Конструктор инициализирующий параметры для подключения к БД
     * с помощью .properties файла
     */
    public ConnectionAdapter() {
        Properties properties = PropertiesInitializer.initialize();
        URL = properties.getProperty("spring.datasource.url");
        USER_NAME = properties.getProperty("spring.datasource.username");
        PASSWORD = properties.getProperty("spring.datasource.password");
    }

    /**
     * Метод предоставлющий инициализированный объект Connection
     * @return Connection
     * @throws SQLException В случае передачи неподходящих параметров
     */
    public  Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
