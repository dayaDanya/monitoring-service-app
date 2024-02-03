package org.ylab.infrastructure.in.db;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Утилита для выполнения миграций базы данных с использованием Liquibase.
 */
public class MigrationUtil {

    private  Properties properties;
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;

    /**
     * Конструктор для инициализации переданных параметров подключения к базе данных.
     *
     * @param URL      URL подключения к базе данных.
     * @param USER_NAME Имя пользователя для аутентификации в базе данных.
     * @param PASSWORD Пароль для аутентификации в базе данных.
     */
    public MigrationUtil(String URL, String USER_NAME, String PASSWORD) {
        this.URL = URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
        this.properties = null;
    }

    /**
     * Конструктор по умолчанию, считывающий параметры подключения к базе данных из файла application.properties.
     * Этот конструктор предполагает, что файл application.properties находится в каталоге src/main/resources.
     */
    public MigrationUtil() {
        this.properties = new Properties();
        try {
            FileInputStream fileInputStream =
                    new FileInputStream(
                            "src/main/resources/application.properties");
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        URL = properties.getProperty("url");
        USER_NAME = properties.getProperty("db-username");
        PASSWORD = properties.getProperty("db-password");
    }

    /**
     * Выполняет миграции базы данных с использованием Liquibase.
     */
    public void migrate() {
        try {
            Connection connection = DriverManager.getConnection(
                    URL,
                    USER_NAME,
                    PASSWORD
            );
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration completed!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
