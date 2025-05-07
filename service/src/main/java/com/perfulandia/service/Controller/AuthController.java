package com.perfulandia.service.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.service.Model.AuthRequest;
import com.perfulandia.service.Servicio.AuthJWT;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthJWT authJWT;

    public AuthController(AuthJWT authJWT) {
        this.authJWT = authJWT;
    }

    @PostMapping("/login")
    public String authenticate(@RequestBody AuthRequest authRequest) {
        String token = authJWT.authenticate(authRequest.getUsername(), authRequest.getPassword());
        if (token != null) {
            return token;
        } else {
            throw new RuntimeException("Invalid credentials");
        }

        ResponseEntity<String> response = new ResponseEntity<>(token, HttpStatus.OK);
        return response;

    }
    

}
