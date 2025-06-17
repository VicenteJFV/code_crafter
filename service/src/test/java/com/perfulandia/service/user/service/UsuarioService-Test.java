package com.perfulandia.service.user.service;

import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Test
    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }


    @Test
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Test
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public boolean existePorId(Long id) {
        return usuarioRepository.existsById(id);
    }

    public void eliminarPorId(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Test
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public boolean verificarPassword(String raw, String hashed) {
        return passwordEncoder.matches(raw, hashed);
    }
}
