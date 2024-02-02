package org.ylab.repositories;

import org.ylab.domain.models.Operation;
import org.ylab.domain.models.enums.Action;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Репозиторий для сущности Operation.
 */
public class OperationRepo {

    Properties properties;
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;


    /**
     * Конструктор, инициализирующий список (вызывается один раз)
     */
    public OperationRepo() {
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

    /**
     * Сохранение операции
     * @param operation Операция для сохранения
     */
    public void save(Operation operation) {
        try (Connection connection = DriverManager
                .getConnection(URL, USER_NAME, PASSWORD)) {
            connection.setAutoCommit(false);
            String insertDataSQL = "INSERT INTO entities.operation (person_id, action, date)" +
                    "VALUES (?, ?, ?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setLong(1, operation.getPersonId());
            insertDataStatement.setString(2, operation.getAction().toString());
            insertDataStatement.setTimestamp(3,
                    Timestamp.valueOf(operation.getDate()));
            insertDataStatement.executeUpdate();
            connection.commit();
            //todo connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение списка всех операций
     * @return Список всех операций
     */
    public Map<Long, Operation> findAll() {
        Map<Long, Operation> operations = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT * FROM entities.operation";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long personId = resultSet.getLong("person_id");
                Action action = Action.valueOf(resultSet.getString("action"));
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                operations.put(id, new Operation( id, personId, action, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }



}
