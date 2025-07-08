package com.perfulandia.service.user.service;

import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.repository.UsuarioRepository;

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

    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public boolean existePorId(Long id) {
        return usuarioRepository.existsById(id);
    }

    public void eliminarPorId(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        System.out.println("Buscando usuario con correo: >" + correo + "<");
        return usuarioRepository.findByCorreo(correo);
    }

    public boolean verificarPassword(String raw, String hashed) {
        return passwordEncoder.matches(raw, hashed);
    }
}
