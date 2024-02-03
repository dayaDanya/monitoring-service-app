package org.ylab.repositories.implementations;

import org.ylab.domain.models.Token;
import org.ylab.repositories.ITokenRepo;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;

/**
 * Реализация интерфейса {@link ITokenRepo} для управления токенами в базе данных.
 */
public class TokenRepo implements ITokenRepo {

    private Properties properties;
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;

    /**
     * Конструктор по умолчанию, который считывает свойства подключения к базе данных из файла application.properties.
     * Этот конструктор предполагает, что файл application.properties находится в каталоге src/main/resources.
     */
    public TokenRepo() {
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
     * Параметризованный конструктор для инициализации TokenRepo с определенными свойствами подключения к базе данных.
     *
     * @param URL      URL подключения к базе данных.
     * @param USER_NAME Имя пользователя для аутентификации в базе данных.
     * @param PASSWORD Пароль для аутентификации в базе данных.
     */
    public TokenRepo(String URL, String USER_NAME, String PASSWORD) {
        this.URL = URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }

    /**
     * Сохраняет токен и связанный с ним идентификатор пользователя в базе данных.
     *
     * @param token    Значение токена для сохранения.
     * @param personId Идентификатор пользователя, связанный с токеном.
     */
    public void saveToken(String token, long personId) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            connection.setAutoCommit(false);
            String insertDataSQL = "insert into entities.token (value, person_id) VALUES (?,?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setString(1, token);
            insertDataStatement.setLong(2, personId);
            insertDataStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обновляет токен в базе данных для заданного идентификатора пользователя.
     *
     * @param token    Новое значение токена.
     * @param personId Идентификатор пользователя, связанный с токеном, который нужно обновить.
     */
    public void updateToken(String token, long personId) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            connection.setAutoCommit(false);
            String updateDataSQL = "update entities.token set value = ? WHERE person_id = ?";
            PreparedStatement updateDataStatement = connection.prepareStatement(updateDataSQL);
            updateDataStatement.setString(1, token);
            updateDataStatement.setLong(2, personId);
            updateDataStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Находит идентификатор пользователя, связанный с заданным токеном в базе данных.
     *
     * @param token Значение токена для поиска.
     * @return {@link Optional}, содержащий идентификатор пользователя при наличии, в противном случае пустой {@link Optional}.
     */
    public Optional<Long> findPersonIdByToken(String token) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT person_id FROM entities.token WHERE value = ?";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(resultSet.getLong("person_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Находит токен по связанному идентификатору пользователя в базе данных.
     *
     * @param personId Идентификатор пользователя для поиска.
     * @return {@link Optional}, содержащий {@link Token} при наличии, в противном случае пустой {@link Optional}.
     */
    public Optional<Token> findByPersonId(Long personId) {
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT * FROM entities.token WHERE person_id = ?";
            PreparedStatement statement = connection.prepareStatement(selectDataSQL);
            statement.setLong(1, personId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Token(resultSet.getLong("id"),
                        resultSet.getLong("person_id"),
                        resultSet.getString("value")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
