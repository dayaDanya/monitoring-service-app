package org.ylab.repositories.implementations;

import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.repositories.ICounterRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * репозиторий счетчиков, то есть слой взаимодействия с бд счетчиков
 * на данный момент реализован с помощью коллекции
 */
public class CounterRepo implements ICounterRepo {

    private ConnectionAdapter connectionAdapter;

    /**
     * конструктор
     */
    public CounterRepo(ConnectionAdapter connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }

    /**
     * сохранение счетчика
     *
     * @param counter
     */
    public void save(Counter counter) {
        try (Connection connection = connectionAdapter
                .getConnection()) {
            connection.setAutoCommit(false);
            String insertDataSQL = "INSERT INTO entities.counter (person_id, counter_type_id, counter_type)" +
                    "VALUES (?, ?, ?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setLong(1, counter.getPersonId());
            insertDataStatement.setLong(2, counter.getCounterTypeId());
            insertDataStatement.setString(3, counter.getCounterType().getName());
            insertDataStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * находит ид счетчика при условии что ид пользователя и тип счетчика соответствует переданным
     *
     * @param personId
     * @param type
     * @return айди счетчика
     */
    public Optional<Long> findIdByPersonIdAndCounterType(long personId, String type) {
        try (Connection connection = connectionAdapter
                .getConnection()) {
            String selectDataSQL = "SELECT id FROM entities.counter " +
                    "where person_id = ? and counter_type = ?";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, personId);
            statement.setString(2, type);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                return Optional.of(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    /**
     * возвращает список айди счетчиков пользователя
     *
     * @param personId
     * @return список айди счетчиков
     */
    public List<Long> findIdsByPersonId(long personId) {
        List<Long> counterIds = new ArrayList<>();
        try (Connection connection = connectionAdapter
                .getConnection()) {
            String selectDataSQL = "SELECT id FROM entities.counter " +
                    "where person_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, personId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                counterIds.add(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counterIds;
    }

    /**
     * возвращает map счетчиков пользователя
     *
     * @param personId айди пользователя
     * @return map счетчиков
     */
    public Map<Long, Counter> findByPersonId(long personId) {
        Map<Long,Counter> counters = new HashMap<>();
        try (Connection connection = connectionAdapter
                .getConnection()) {
            String selectDataSQL = "SELECT * FROM entities.counter " +
                    "where person_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, personId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long pId = resultSet.getLong("person_id");
                long cId = resultSet.getLong("counter_type_id");
                String counterType = resultSet.getString("counter_type");
                counters.put(id, new Counter(id, pId, cId, new CounterType(counterType)));
            }
        return counters;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counters;


    }


}
