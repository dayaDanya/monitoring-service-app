package org.ylab.usecases;

import org.ylab.exceptions.TokenNotActualException;
import org.ylab.repositories.PersonRepo;
import org.ylab.repositories.TokenRepo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

/**
 * Класс, представляющий сервис для работы с токенами.
 */
//todo перенести в бд
public class TokenService {
    private TokenRepo tokenRepo;

    private PersonRepo personRepo;

    public TokenService(TokenRepo tokenRepo, PersonRepo personRepo) {
        this.tokenRepo = tokenRepo;
        this.personRepo = personRepo;
    }

    /**
     * Метод для генерации токена и связывания его с идентификатором пользователя.
     *
     * @param personId Идентификатор пользователя, с которым связывается токен.
     * @return Сгенерированный токен.
     */
    public String getToken(long personId)  {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hashedBytes = messageDigest.digest(String.valueOf(personId).getBytes());
        String token = UUID.nameUUIDFromBytes(hashedBytes).toString();
        if(tokenRepo.findIdByPersonId(personId).isEmpty())
            tokenRepo.saveToken(token, personId);
        else
            tokenRepo.updateToken(token, personId);
        return token;
    }

    /**
     * Метод для получения идентификатора пользователя по токену.
     *
     * @param token Токен, для которого требуется получить идентификатор пользователя.
     * @return Идентификатор пользователя, связанный с указанным токеном.
     * @throws TokenNotActualException Выбрасывается, если токен не актуален (не существует).
     */
    public long getPersonId(String token) throws TokenNotActualException {
        Optional<Long> personId = tokenRepo.findPersonIdByToken(token);
        return personId.orElseThrow(TokenNotActualException::new);
    }

}
