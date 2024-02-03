package org.ylab.repositories.implementations;

import org.ylab.domain.models.Person;
import org.ylab.domain.models.enums.Role;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.repositories.IPersonRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Репозиторий для сущности Person.
 */
public class PersonRepo implements IPersonRepo {
    private ConnectionAdapter connectionAdapter;

    /**
     * конструктор
     */
    public PersonRepo(ConnectionAdapter connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }

    /**
     * Поиск пользователя по электронной почте.
     *
     * @param email Электронная почта пользователя
     * @return Пользователь (Optional)
     */
    public Optional<Person> findByEmail(String email) {

        try (Connection connection = connectionAdapter
                .getConnection()) {
            String selectDataSQL = "SELECT * FROM entities.person WHERE email = ? ";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Person(
                        resultSet.getLong("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role"))));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }

    /**
     * Поиск идентификатора пользователя по электронной почте.
     *
     * @param email Электронная почта пользователя
     * @return Идентификатор пользователя (Optional)
     */
    public Optional<Long> findIdByEmail(String email) {
        try (Connection connection = connectionAdapter
                .getConnection()) {
            String selectDataSQL = "SELECT id FROM entities.person WHERE email = ? ";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSet.getLong("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Сохранение пользователя.
     *
     * @param person Пользователь для сохранения
     */
    public void save(Person person) {
        try (Connection connection = connectionAdapter
                .getConnection()) {
            connection.setAutoCommit(false);
            String insertDataSQL = "INSERT INTO entities.person (email, password, role)" +
                    "VALUES (?, ?, ?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setString(1, person.getEmail());
            insertDataStatement.setString(2, person.getPassword());
            insertDataStatement.setString(3, person.getRole().toString());
            insertDataStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Поиск пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя
     * @return Пользователь (Optional)
     */
    public Optional<Person> findById(long id) {
        try (Connection connection = connectionAdapter
                .getConnection()) {
            String selectDataSQL = "SELECT * FROM entities.person WHERE id = ? ";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Person(
                        resultSet.getLong("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role"))));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }



}
