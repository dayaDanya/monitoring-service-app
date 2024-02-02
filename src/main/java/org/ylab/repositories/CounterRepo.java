package org.ylab.repositories;

import org.ylab.domain.models.Counter;
import org.ylab.domain.models.CounterType;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * репозиторий счетчиков, то есть слой взаимодействия с бд счетчиков
 * на данный момент реализован с помощью коллекции
 */
public class CounterRepo {

    Properties properties;
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;

    /**
     * конструктор, инициализирует список единожды
     */
    public CounterRepo() {
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
     * сохранение счетчика
     *
     * @param counter
     */
    public void save(Counter counter) {
        try (Connection connection = DriverManager
                .getConnection(URL, USER_NAME, PASSWORD)) {
            connection.setAutoCommit(false);
            String insertDataSQL = "INSERT INTO entities.counter (person_id, counter_type_id, counter_type)" +
                    "VALUES (?, ?, ?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setLong(1, counter.getPersonId());
            insertDataStatement.setLong(2, counter.getCounterTypeId());
            insertDataStatement.setString(3, counter.getCounterType().getName());
            insertDataStatement.executeUpdate();
            connection.commit();
            //todo connection.rollback();
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
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
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
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT id FROM entities.counter " +
                    "where person_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, personId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                counterIds.add(id);
            }
            //todo удалить здесь и во всех похожих возврат collections
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counterIds;
    }

    /**
     * возвращает список счетчиков пользователя
     *
     * @param personId
     * @return список счетчиков
     */
    public Map<Long, Counter> findByPersonId(long personId) {
        Map<Long,Counter> counters = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
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
