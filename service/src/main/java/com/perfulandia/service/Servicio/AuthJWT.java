package com.perfulandia.service.Servicio;

import org.springframework.stereotype.Service;

import com.perfulandia.service.Client.UserClient;
import com.perfulandia.service.Util.BcryptPasswordEncoder;
import com.perfulandia.service.Util.JwtUtil;
import com.perfulandia.service.Model.AuthUser;
import com.perfulandia.service.Repository.AuthUserRepository;
@Service
public class AuthJWT {
    private final UserClient userClient;
    private final AuthUserRepository authUserRepository;
    private final JwtUtil jwtUtil;
    private final BcryptPasswordEncoder passwordEncoder;

    public AuthJWT(UserClient userClient, AuthUserRepository authUserRepository, JwtUtil jwtUtil, BcryptPasswordEncoder passwordEncoder) {
        this.userClient = userClient;
        this.authUserRepository = authUserRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String username, String password) {
        AuthUser authUser = authUserRepository.findByUsername(username);
        if (authUser != null && passwordEncoder.matches(password, authUser.getPassword())) {
            return jwtUtil.generateToken(authUser);
        }
        return null;
    }
    public AuthUser getUserFromToken(String token) {
        String username = jwtUtil.extractUsername(token);
        return authUserRepository.findByUsername(username);
    }

}
