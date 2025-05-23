package com.perfulandia.service.servicio;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.perfulandia.service.common.Role;
import com.perfulandia.service.model.AuthRequest;
import com.perfulandia.service.model.AuthUser;
import com.perfulandia.service.repository.AuthRepository;
import com.perfulandia.service.repository.AuthService;

@Component
@Service
public  class AuthserviceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthserviceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    @Override
        public AuthUser register(AuthRequest authRequest) {
        if (authRepository.existsByUsername(authRequest.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        AuthUser newUser = new AuthUser();
        newUser.setUsername(authRequest.getUsername());
        newUser.setPassword(encodePassword(authRequest.getPassword()));
        newUser.setRole(Role.USER);

        return authRepository.save(newUser);
    }

    private String encodePassword(String password) {
        // TODO Auto-generated method stub
        return passwordEncoder.encode(password);
    }

    @Override
    public String authenticate(String username, String password) {
        AuthUser user = authRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return JwtUtil.generateToken(user);
        }
        throw new BadCredentialsException("Credenciales incorrectas");
    }

    @Override
    public boolean logout(String username) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        
        return username != null && !password.isEmpty();
    }

    @Override
    public String authenticate(Object object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
