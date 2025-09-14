package com.it355.movie_management.utils;

import com.it355.movie_management.common.config.AppConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
    private final Key key;
    private final long EXPIRATION_MS;

    public JWTUtil(AppConfig config) {
        this.key = Keys.hmacShaKeyFor(config.getJwtSecret().getBytes(StandardCharsets.UTF_8));
        this.EXPIRATION_MS = 24 * 60 * 60 * 1000;
    }

    public String generateToken(Long userId, String username, int roleId) {
        return Jwts.builder()
                .claim("id", userId)
                .claim("username", username)
                .claim("roleId", roleId)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
    }
}