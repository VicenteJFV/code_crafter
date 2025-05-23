package com.perfulandia.service.security;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.perfulandia.service.model.AuthUser;

import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {
    private String secret;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(AuthUser user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date(0))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(null)
                .compact();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

}
