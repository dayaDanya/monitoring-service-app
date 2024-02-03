package org.ylab.repositories;

import org.ylab.domain.models.Token;

import java.util.Optional;

public interface ITokenRepo {

    //todo doc
    public void saveToken(String token, long personId);
    void updateToken(String token, long personId);
    Optional<Long> findPersonIdByToken(String token);
    Optional<Token> findByPersonId(Long personId);
}
