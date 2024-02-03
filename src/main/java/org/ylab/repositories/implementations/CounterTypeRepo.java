package org.ylab.repositories.implementations;

import org.ylab.domain.models.CounterType;
import org.ylab.repositories.ICounterTypeRepo;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;

/**
 * Репозиторий типов счетчиков, представляющий слой взаимодействия с базой данных типов счетчиков.
 * На текущий момент реализован с использованием коллекции.
 */
public class CounterTypeRepo implements ICounterTypeRepo {

    Properties properties;
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;



    /**
     * конструктор
     */
    //todo migration
    public CounterTypeRepo() {
        properties = new Properties();
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

    public CounterTypeRepo(String URL, String USER_NAME, String PASSWORD) {
        this.URL = URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }

    /**
     * Сохранение типа счетчика
     *
     * @param type Тип счетчика для сохранения
     */
    public void save(CounterType type) {
        try (Connection connection = DriverManager
                .getConnection(URL, USER_NAME, PASSWORD)) {
            connection.setAutoCommit(false);
            String insertDataSQL = "INSERT INTO entities.counter_type" +
                    " (name) VALUES (?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setString(1, type.getName());
            insertDataStatement.executeUpdate();
            connection.commit();
            //todo connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Поиск типа счетчика по его имени
     *
     * @param name Имя типа счетчика
     * @return Тип счетчика (Optional)
     */
    public Optional<CounterType> findOne(String name) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT * FROM entities.counter_type WHERE name = ? ";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new CounterType(
                        resultSet.getLong("id"),
                        resultSet.getString("name")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}