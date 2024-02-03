package org.ylab.repositories;

import org.ylab.domain.models.Token;

import java.util.Optional;

/**
 * Интерфейс для работы с токенами пользователей.
 */
public interface ITokenRepo {

    /**
     * Сохраняет токен для указанного идентификатора пользователя.
     *
     * @param token     Токен для сохранения.
     * @param personId  Идентификатор пользователя.
     */
    public void saveToken(String token, long personId);

    /**
     * Обновляет существующий токен для указанного идентификатора пользователя.
     *
     * @param token     Новый токен для обновления.
     * @param personId  Идентификатор пользователя.
     */
    void updateToken(String token, long personId);

    /**
     * Находит идентификатор пользователя по заданному токену.
     *
     * @param token Токен для поиска.
     * @return Optional, содержащий идентификатор пользователя, если токен найден, иначе пустой Optional.
     */
    Optional<Long> findPersonIdByToken(String token);

    /**
     * Находит токен по заданному идентификатору пользователя.
     *
     * @param personId Идентификатор пользователя.
     * @return Optional, содержащий токен пользователя, если идентификатор найден, иначе пустой Optional.
     */
    Optional<Token> findByPersonId(Long personId);
}
