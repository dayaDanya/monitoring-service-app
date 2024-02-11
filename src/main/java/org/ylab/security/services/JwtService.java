package org.ylab.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import jakarta.servlet.http.HttpServletRequest;
import org.ylab.exceptions.TokenNotActualException;

import java.util.Optional;

public class JwtService {
    private static final SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    public static String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .signWith(key)
                .compact();
        //todo expiration
    }

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
