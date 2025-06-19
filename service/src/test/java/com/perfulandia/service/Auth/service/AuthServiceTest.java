package com.perfulandia.service.Auth.service;
import org.junit.jupiter.api.Test;

import com.perfulandia.service.user.model.Rol;
import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.repository.RolRepository;
import com.perfulandia.service.user.service.UsuarioService;

import java.util.List;
import java.util.Optional;

import org.junit.Before;    
    
public class AuthServiceTest {

    @Before
    public void setup(){

    }
        
    @Test
    public void test() {
        
    }

    public class AuthService {

    private final RolRepository rolRepository = null;
    private final UsuarioService usuarioService = null;

    @Test
    public List<Rol> obtenerRoles() {
        return rolRepository.findAll()
                .stream()
                .sorted((r1, r2) -> r1.getNombre().compareToIgnoreCase(r2.getNombre()))
                .toList();
    }

    @Test
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


}
    