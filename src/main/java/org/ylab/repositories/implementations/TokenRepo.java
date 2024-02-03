package org.ylab.repositories.implementations;

import org.ylab.domain.models.Token;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.repositories.ITokenRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Реализация интерфейса {@link ITokenRepo} для управления токенами в базе данных.
 */
public class TokenRepo implements ITokenRepo {

    private ConnectionAdapter connectionAdapter;

    /**
     * конструктор
     */
    public TokenRepo(ConnectionAdapter connectionAdapter) {
        this.connectionAdapter = connectionAdapter;
    }



    /**
     * Сохраняет токен и связанный с ним идентификатор пользователя в базе данных.
     *
     * @param token    Значение токена для сохранения.
     * @param personId Идентификатор пользователя, связанный с токеном.
     */
    public void saveToken(String token, long personId) {
        try (Connection connection = connectionAdapter
                .getConnection()) {
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
        try (Connection connection = connectionAdapter
                .getConnection()) {
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
        try (Connection connection = connectionAdapter
                .getConnection()) {
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
        try (Connection connection = connectionAdapter
                .getConnection()) {
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
