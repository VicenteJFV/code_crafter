package com.perfulandia.service.service;

import com.perfulandia.service.model.Usuario;
import com.perfulandia.service.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
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

    public boolean verificarPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

}
