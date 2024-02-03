package org.ylab.repositories;

import org.ylab.domain.models.Token;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;

public class TokenRepo {
    Properties properties;
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;


    /**
     * Конструктор
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

    public TokenRepo(String URL, String USER_NAME, String PASSWORD) {
        this.URL = URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }
    //todo во всех репо пробрасывать исключение в сервис
    //todo doc
    public void saveToken(String token, long personId){
        try (Connection connection = DriverManager
                .getConnection(URL, USER_NAME, PASSWORD)) {
            connection.setAutoCommit(false);
            String insertDataSQL = "insert into entities.token (value, person_id)" +
                    " VALUES(?,?)";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setString(1, token);
            insertDataStatement.setLong(2, personId);
            insertDataStatement.executeUpdate();
            connection.commit();
            //todo connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateToken(String token, long personId){
        try (Connection connection = DriverManager
                .getConnection(URL, USER_NAME, PASSWORD)) {
            connection.setAutoCommit(false);
            String insertDataSQL = "update entities.token set value = ?" +
                    " WHERE person_id = ?";
            PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL);
            insertDataStatement.setString(1, token);
            insertDataStatement.setLong(2, personId);
            insertDataStatement.executeUpdate();
            connection.commit();
            //todo connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Optional<Long> findPersonIdByToken(String token){
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT person_id FROM entities.token WHERE value = ? ";
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
    public Optional<Token> findByPersonId(Long personId){
        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD)) {
            String selectDataSQL = "SELECT * FROM entities.token WHERE person_id = ? ";
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
