package org.ylab.repositories;

import org.ylab.domain.models.Measurement;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Репозиторий измерений, представляющий слой взаимодействия с базой данных измерений.
 *
 */
public class MeasurementRepo {
    Properties properties;
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;


    /**
     * Конструктор, инициализирующий список (вызывается один раз)
     */
    public MeasurementRepo() {
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
     * Сохранение измерения
     * @param measurement Измерение для сохранения
     */
    public void save(Measurement measurement) {
        try (Connection connection = DriverManager
                .getConnection(URL, USER_NAME, PASSWORD)) {
            connection.setAutoCommit(false);
            String insertDataSQL = "INSERT INTO entities.measurement (counter_id, amount, submission_date," +
                    " counter_type)" +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setLong(1, measurement.getCounterId());
            insertDataStatement.setDouble(2, measurement.getCounterId());
            insertDataStatement.setTimestamp(3,
                    Timestamp.valueOf(measurement.getSubmissionDate()));
            insertDataStatement.setString(4, measurement.getCounterType());
            insertDataStatement.executeUpdate();
            connection.commit();
            //todo connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Поиск последней даты измерения для заданного счетчика
     * @param counterId Идентификатор счетчика
     * @return Последняя дата измерения (Optional)
     */
    public Optional<LocalDateTime> findLastDate(long counterId) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT submission_date FROM entities.measurement WHERE counter_id = ? " +
                    "ORDER BY submission_date DESC LIMIT 1;";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, counterId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                return Optional.of(date);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Поиск последнего измерения для заданного счетчика
     * @param counterId Идентификатор счетчика
     * @return Последнее измерение (Optional)
     */
    public Optional<Measurement> findLast(long counterId) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT * FROM entities.measurement WHERE counter_id = ? " +
                    "ORDER BY submission_date DESC LIMIT 1;";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, counterId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                long cId = resultSet.getLong("counter_id");
                double amount = resultSet.getDouble("amount");
                String counterType = resultSet.getString("counter_type");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                return Optional.of(new Measurement(id, amount, date, counterType, cId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Поиск всех измерений для заданного счетчика
     * @param counterId Идентификатор счетчика
     * @return Список всех измерений для заданного счетчика
     */
    public List<Measurement> findAllByCounterId(long counterId) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT * FROM entities.measurements " +
                    "where counter_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, counterId);
            ResultSet resultSet = statement.executeQuery();
            List<Measurement> measurements = new ArrayList<>();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                long cId = resultSet.getLong("counter_id");
                double amount = resultSet.getDouble("amount");
                String counterType = resultSet.getString("counter_type");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                measurements.add(new Measurement(id, amount, date, counterType, cId));
            }
            return measurements;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


    /**
     * Поиск всех измерений
     * @return Список всех измерений
     */
    public List<Measurement> findAll() {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT * FROM entities.measurements";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            ResultSet resultSet = statement.executeQuery();
            List<Measurement> measurements = new ArrayList<>();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                long counterId = resultSet.getLong("counter_id");
                double amount = resultSet.getDouble("amount");
                String counterType = resultSet.getString("counter_type");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                measurements.add(new Measurement(id,amount, date, counterType, counterId));
            }
            return measurements;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * Поиск измерения для заданного счетчика и месяца
     * @param counterId Идентификатор счетчика
     * @param month Месяц (1-12)
     * @return Измерение для заданного счетчика и месяца (Optional)
     */
    public Optional<Measurement> findByMonth(long counterId, int month) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT * FROM entities.measurements " +
                    "where counter_id = ?, EXTRACT(MONTH FROM your_timestamp_column) = ?";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, counterId);
            statement.setInt(2, month);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                long cId = resultSet.getLong("counter_id");
                double amount = resultSet.getDouble("amount");
                String counterType = resultSet.getString("counter_type");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();

                return Optional.of(new Measurement(id, amount, date, counterType, cId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
