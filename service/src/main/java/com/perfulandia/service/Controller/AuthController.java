package com.perfulandia.service.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.service.model.AuthRequest;
import com.perfulandia.service.model.AuthUser;
import com.perfulandia.service.repository.AuthService;

@RestController
@RequestMapping("/auth")
public class Authcontroller<Authrequest> {

    private final AuthService authService;
    @SuppressWarnings("unused")
    private Object authRepository;

    @Autowired
    public Authcontroller(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {

        if (authRequest.getUsername() == null || authRequest.getPassword() == null) {

            return ResponseEntity.badRequest().body("Credenciales incompletas");
        }

        boolean isValid = authService.validateCredentials(
                authRequest.getUsername(),
                authRequest.getPassword());

        return isValid
                ? ResponseEntity.ok("Login exitoso")
                : ResponseEntity.status(401).body("Credenciales inválidas");
    }

    }

    @PostMapping("/register")
    public ResponseEntity<AuthUser> register(@RequestBody AuthRequest request) {

        AuthUser newUser = new AuthUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
