package org.ylab.usecases;

import org.ylab.util.TokenNotActualException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Класс, представляющий сервис для работы с токенами.
 */
public class TokenService {
    private static TokenService INSTANCE;
    private final Map<String, Long> tokens;

    /**
     * Приватный конструктор, инициализирующий хранилище токенов.
     */
    private TokenService() {
        tokens = new HashMap<>();
    }

    /**
     * Метод для получения единственного экземпляра класса TokenService (реализация Singleton).
     *
     * @return Единственный экземпляр класса TokenService.
     */
    public static TokenService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TokenService();
        }
        return INSTANCE;
    }

    /**
     * Метод для генерации токена и связывания его с идентификатором пользователя.
     *
     * @param personId Идентификатор пользователя, с которым связывается токен.
     * @return Сгенерированный токен.
     */
    public String getToken(long personId) {
        String uuid = UUID.randomUUID().toString();
        tokens.put(uuid, personId);
        return uuid;
    }

    /**
     * Метод для получения идентификатора пользователя по токену.
     *
     * @param token Токен, для которого требуется получить идентификатор пользователя.
     * @return Идентификатор пользователя, связанный с указанным токеном.
     * @throws TokenNotActualException Выбрасывается, если токен не актуален (не существует).
     */
    public long getPersonId(String token) throws TokenNotActualException {
        Long personId = tokens.get(token);
        if (personId != null) {
            return personId;
        } else {
            throw new TokenNotActualException();
        }
    }

    /**
     * Метод для удаления токена из хранилища.
     *
     * @param token Токен, который требуется удалить.
     * @return Идентификатор пользователя, связанный с удаленным токеном.
     */
    public Long deleteToken(String token) {
        return tokens.remove(token);
    }
}
