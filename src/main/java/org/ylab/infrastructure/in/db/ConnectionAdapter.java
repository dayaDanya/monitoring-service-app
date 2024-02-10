package org.ylab.infrastructure.in.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionAdapter {
    private Properties properties;
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;

    public ConnectionAdapter(String URL, String USER_NAME, String PASSWORD) {
        this.URL = URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }

    public ConnectionAdapter() {
        properties = PropertiesInitializer.initialize();
        URL = properties.getProperty("url");
        USER_NAME = properties.getProperty("db-username");
        PASSWORD = properties.getProperty("db-password");
    }
    public  Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
