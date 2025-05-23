package com.perfulandia.service.Auth.service;

import com.perfulandia.service.user.model.Rol;
import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.repository.RolRepository;
import com.perfulandia.service.user.service.UsuarioService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RolRepository rolRepository;
    private final UsuarioService usuarioService;

    public List<Rol> obtenerRoles() {
        return rolRepository.findAll()
                .stream()
                .sorted((r1, r2) -> r1.getNombre().compareToIgnoreCase(r2.getNombre()))
                .toList();
    }

    public Optional<Usuario> validarCredenciales(String correo, String password) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorCorreo(correo);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (usuarioService.verificarPassword(password, usuario.getPassword())) {
                return Optional.of(usuario);
            }
        }

        return Optional.empty();
    }
}
