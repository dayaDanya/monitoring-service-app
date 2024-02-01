package org.ylab.repositories;

import org.ylab.domain.models.Person;
import org.ylab.domain.models.enums.Role;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;

/**
 * Репозиторий для сущности Person.
 */
public class PersonRepo {
    Properties properties;
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;


    /**
     * Конструктор, инициализирующий список (вызывается один раз).
     * Создается администратор по умолчанию.
     */
    public PersonRepo() {
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
        //todo админ

//            save(new Person("admin",
//                    "agSlkyvn99dVB6pKeD6MVA==:EvJ6J8w/LwveTJ+WA5AgTu9OzI7+FNakNFuZO8JqDCM2OUWL/82UXOAwVjPEpkxH5Apw65Pdp8iogVfDwIXbDQ==", Role.ADMIN));

    }

    /**
     * Поиск пользователя по электронной почте.
     * @param email Электронная почта пользователя
     * @return Пользователь (Optional)
     */
    public Optional<Person> findByEmail(String email) {
        //todo не делать новые стейтменты каждый раз
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
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
     * @param email Электронная почта пользователя
     * @return Идентификатор пользователя (Optional)
     */
    public Optional<Long> findIdByEmail(String email) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
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
     * @param person Пользователь для сохранения
     */
    public void save(Person person) {
        try (Connection connection = DriverManager
                .getConnection(URL, USER_NAME, PASSWORD)) {
            connection.setAutoCommit(false);
            String insertDataSQL = "INSERT INTO entities.person (email, password, role)" +
                    "VALUES (?, ?, ?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setString(1, person.getEmail());
            insertDataStatement.setString(2, person.getPassword());
            insertDataStatement.setString(3, person.getRole().toString());
            insertDataStatement.executeUpdate();
            connection.commit();
            //todo connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Поиск пользователя по идентификатору.
     * @param id Идентификатор пользователя
     * @return Пользователь (Optional)
     */
    public Optional<Person> findById(long id) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
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
