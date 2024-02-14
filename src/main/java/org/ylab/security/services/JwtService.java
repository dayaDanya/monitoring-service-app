package org.ylab.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import jakarta.servlet.http.HttpServletRequest;
import org.ylab.exceptions.TokenNotActualException;

import java.util.Optional;

/**
 * Сервис генерации JWT-токенов
 */
public class JwtService {
    /**
     * секретный ключ
     */
    private static final SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    /**
     * Метод генерирующий токен на основе данных пользователя
     * @param subject данные пользователя
     * @return jwt-токен
     */
    public static String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)                         
                .signWith(key)
                .compact();
        //todo expiration
    }

    /**
     * сервер проверяющий токен
     * @param request запрос
     * @return id пользователя
     * @exception TokenNotActualException в случае если переданный запрос не прошел проверку
     */
    public static Long validateTokenAndGetSubject(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token);
                return Long.valueOf(claimsJws.getBody().getSubject());
            } catch (Exception e) {
                throw new TokenNotActualException();
            }
        }
        throw new TokenNotActualException();
    }
}
