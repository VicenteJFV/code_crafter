package com.perfulandia.service.repository;
import com.perfulandia.service.model.AuthRequest;
import com.perfulandia.service.model.AuthUser;

public interface AuthService {
    AuthUser register(AuthRequest authRequest);

    String authenticate(String username, String password);

    boolean logout(String username);

    boolean validateCredentials(String username, String password);
    

    public String authenticate(Object object);


    public record register() {

        
    }
}


