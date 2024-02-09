package org.ylab.repositories.implementations;

import org.ylab.domain.models.Operation;
import org.ylab.domain.enums.Action;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.repositories.IOperationRepo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Репозиторий для сущности Operation.
 */
public class OperationRepo implements IOperationRepo {

    private ConnectionAdapter connectionAdapter;

//    Properties properties;
//    private final String URL;
//    private final String USER_NAME;
//    private final String PASSWORD;


    /**
     * Конструктор
     */
    public OperationRepo(ConnectionAdapter connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }

    /**
     * Сохранение операции
     * @param operation Операция для сохранения
     */
    public void save(Operation operation) {
        try (Connection connection = connectionAdapter
                .getConnection()) {
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
        try (Connection connection = connectionAdapter.getConnection()) {
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
