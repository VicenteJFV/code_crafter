
package com.perfulandia.service.user.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private final Long id = 1L;
    private final String correo = "test@example.com";
    private final String password = "password123";
    private final String passwordEncriptada = "$2a$10$hashedPassword";

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(id);
        usuario.setCorreo(correo);
        usuario.setPassword(password);
    }

    @Test
    void guardarUsuario() {
        // Configurar comportamiento
        when(passwordEncoder.encode(password)).thenReturn(passwordEncriptada);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // Ejecutar método
        Usuario resultado = usuarioService.guardarUsuario(usuario);

        // Verificar
        verify(passwordEncoder).encode(password);
        verify(usuarioRepository).save(usuario);
        assertEquals(passwordEncriptada, usuario.getPassword());
        assertEquals(id, resultado.getId());
    }

    @Test
    void listarUsuarios() {
        // Configurar
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Ejecutar
        List<Usuario> resultado = usuarioService.listarUsuarios();

        // Verificar
        assertEquals(1, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void buscarPorId() {
        // Configurar
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Ejecutar
        Optional<Usuario> resultado = usuarioService.buscarPorId(99L);

        // Verificar
        assertTrue(resultado.isEmpty());
    }

    @Test
    void existePorId() {
        // Configurar
        when(usuarioRepository.existsById(id)).thenReturn(true);

        // Ejecutar
        boolean resultado = usuarioService.existePorId(id);

        // Verificar
        assertTrue(resultado);
    }

    @Test
    void eliminarPorId() {
        // Configurar
        doNothing().when(usuarioRepository).deleteById(id);

        // Ejecutar
        usuarioService.eliminarPorId(id);

        // Verificar
        verify(usuarioRepository).deleteById(id);
    }

    @Test
    void buscarPorCorreo() {
        // Configurar
        when(usuarioRepository.findByCorreo(correo)).thenReturn(Optional.of(usuario));

        // Ejecutar
        Optional<Usuario> resultado = usuarioService.buscarPorCorreo(correo);

        // Verificar
        assertTrue(resultado.isPresent());
        assertEquals(correo, resultado.get().getCorreo());
    }

    @Test
    void verificarPassword() {
        // Configurar
        when(passwordEncoder.matches(password, passwordEncriptada)).thenReturn(true);

        // Ejecutar
        boolean resultado = usuarioService.verificarPassword(password, passwordEncriptada);

        // Verificar
        assertTrue(resultado);
        verify(passwordEncoder).matches(password, passwordEncriptada);
    }

}