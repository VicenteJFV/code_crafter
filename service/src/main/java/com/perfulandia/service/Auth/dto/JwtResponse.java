package com.perfulandia.service.Auth.dto;

public class JwtResponse {
    private String token;

    // ✅ Constructor que recibe el token
    public JwtResponse(String token) {
        this.token = token;
    }

    // Getter y Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
