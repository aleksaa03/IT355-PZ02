package com.it355.movie_management.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JWTUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000;

    public static String generateToken(Long userId, String username, int roleId) {
        return Jwts.builder()
                .claim("id", userId)
                .claim("username", username)
                .claim("roleId", roleId)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
    }
}