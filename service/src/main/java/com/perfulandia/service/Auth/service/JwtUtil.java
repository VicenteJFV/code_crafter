package com.perfulandia.service.Auth.service;

import com.perfulandia.service.config.JwtConfig;
import com.perfulandia.service.model.Usuario;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.*;

@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generarToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getId());
        claims.put("rol", usuario.getRol().getNombre());

        SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getCorreo())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String obtenerCorreoDesdeToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validarToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extraerIdDesdeToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.get("id").toString());
    }

    public String extraerRolDesdeToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("rol").toString();
    }

}