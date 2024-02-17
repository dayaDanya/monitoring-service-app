package org.ylab.repositories.implementations;

import org.springframework.stereotype.Repository;
import org.ylab.domain.models.CounterType;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.repositories.ICounterTypeRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Репозиторий типов счетчиков, представляющий слой взаимодействия с базой данных типов счетчиков.
 *
 */
@Repository
public class CounterTypeRepo implements ICounterTypeRepo {

    private final ConnectionAdapter connectionAdapter;

    /**
     * конструктор
     */
    public CounterTypeRepo(ConnectionAdapter connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }

    /**
     * Сохранение типа счетчика
     *
     * @param type Тип счетчика для сохранения
     */
    public void save(CounterType type) {
        try (Connection connection = connectionAdapter
                .getConnection()) {
            connection.setAutoCommit(false);
            String insertDataSQL = "INSERT INTO entities.counter_type" +
                    " (name) VALUES (?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setString(1, type.getName());
            insertDataStatement.executeUpdate();
            connection.commit();

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
        try (Connection connection = connectionAdapter
                .getConnection()) {
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