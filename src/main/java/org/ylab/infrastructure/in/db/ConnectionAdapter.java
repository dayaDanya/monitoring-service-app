package org.ylab.infrastructure.in.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionAdapter {
    Properties properties;
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;

    public ConnectionAdapter(String URL, String USER_NAME, String PASSWORD) {
        this.URL = URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }

    public ConnectionAdapter() {
        properties = new Properties();
        try {
            FileInputStream fileInputStream =
                    new FileInputStream(
                            "/home/danya/IdeaProjects/monitoring-service-app-web/src/main/resources/application.properties");
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
