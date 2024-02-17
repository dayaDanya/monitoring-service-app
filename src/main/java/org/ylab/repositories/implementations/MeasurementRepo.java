package org.ylab.repositories.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.ylab.domain.models.Measurement;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.repositories.IMeasurementRepo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Репозиторий измерений, представляющий слой взаимодействия с базой данных измерений.
 *
 */
@Repository
public class MeasurementRepo implements IMeasurementRepo {
    private final ConnectionAdapter connectionAdapter;

    /**
     * конструктор
     */
    @Autowired
    public MeasurementRepo(ConnectionAdapter connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }

    /**
     * Сохранение измерения
     * @param measurement Измерение для сохранения
     */
    public void save(Measurement measurement) {
        try (Connection connection = connectionAdapter
                .getConnection()) {
            connection.setAutoCommit(false);
            String insertDataSQL = "INSERT INTO entities.measurement (counter_id, amount, submission_date," +
                    " counter_type)" +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setLong(1, measurement.getCounterId());
            insertDataStatement.setDouble(2, measurement.getAmount());
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
        try (Connection connection = connectionAdapter
                .getConnection()) {
            String selectDataSQL = "SELECT submission_date FROM entities.measurement WHERE counter_id = ? " +
                    "ORDER BY submission_date DESC LIMIT 1;";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, counterId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                LocalDateTime date = resultSet.getTimestamp("submission_date").toLocalDateTime();

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
        try (Connection connection = connectionAdapter
                .getConnection()) {
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
                LocalDateTime date = resultSet.getTimestamp("submission_date").toLocalDateTime();

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
    public Map<Long,Measurement> findAllByCounterId(long counterId) {
        Map<Long, Measurement> measurements = new HashMap<>();
        try (Connection connection = connectionAdapter
                .getConnection()) {
            String selectDataSQL = "SELECT * FROM entities.measurement " +
                    "where counter_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, counterId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long cId = resultSet.getLong("counter_id");
                double amount = resultSet.getDouble("amount");
                String counterType = resultSet.getString("counter_type");
                LocalDateTime date = resultSet.getTimestamp("submission_date").toLocalDateTime();

                measurements.put(id, new Measurement(id, amount, date, counterType, cId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return measurements;
    }


    /**
     * Поиск всех измерений
     * @return Список всех измерений
     */
    public Map<Long, Measurement> findAll() {
        Map<Long, Measurement> measurements = new HashMap<>();
        try (Connection connection = connectionAdapter
                .getConnection()) {
            String selectDataSQL = "SELECT * FROM entities.measurement";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long counterId = resultSet.getLong("counter_id");
                double amount = resultSet.getDouble("amount");
                String counterType = resultSet.getString("counter_type");
                LocalDateTime date = resultSet.getTimestamp("submission_date").toLocalDateTime();

                measurements.put(id, new Measurement(id,amount, date, counterType, counterId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return measurements;
    }

    /**
     * Поиск измерения для заданного счетчика и месяца
     * @param counterId Идентификатор счетчика
     * @param month Месяц (1-12)
     * @return Измерение для заданного счетчика и месяца (Optional)
     */
    public Optional<Measurement> findByMonth(long counterId, int month) {
        try (Connection connection = connectionAdapter
                .getConnection()) {
            String selectDataSQL = "SELECT * FROM entities.measurement " +
                    "where counter_id = ? and EXTRACT(MONTH FROM submission_date) = ?";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, counterId);
            statement.setInt(2, month);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                long cId = resultSet.getLong("counter_id");
                double amount = resultSet.getDouble("amount");
                String counterType = resultSet.getString("counter_type");
                LocalDateTime date = resultSet.getTimestamp("submission_date").toLocalDateTime();

                return Optional.of(new Measurement(id, amount, date, counterType, cId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
