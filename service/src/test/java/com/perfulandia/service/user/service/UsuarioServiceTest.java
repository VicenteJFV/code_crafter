package com.perfulandia.service.user.service;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;    
    
public class UsuarioServiceTest {

    
    private final UsuarioRepository usuarioRepository = null;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void  guardarUsuario() {
        
        Usuario usuario = new Usuario();
        usuario.setPassword("1234");
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
    }



   

}

    

    