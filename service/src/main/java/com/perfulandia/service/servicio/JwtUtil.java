package com.perfulandia.service.servicio;

import com.perfulandia.service.model.AuthUser;

public class JwtUtil {
    public static String generateToken(AuthUser authUser) {
        // Implementación de generación de token JWT
        if (authUser == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        } else if (authUser.getUsername() == null || authUser.getUsername().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío");
        } else if (authUser.getEmail() == null || ((String) authUser.getEmail()).isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico no puede ser nulo o vacío");
        }

        return "token";

    }

    @Override
    public String toString() {
        return "JwtUtil []";
    }

    public String extractUsername(String token) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}