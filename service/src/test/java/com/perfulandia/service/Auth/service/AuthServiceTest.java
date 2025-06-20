package com.perfulandia.service.Auth.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.perfulandia.service.user.model.Rol;
import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.repository.RolRepository;
import com.perfulandia.service.user.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private RolRepository rolRepository;
    
    @Mock
    private UsuarioService usuarioService;
    
    @InjectMocks
    private AuthService authService;
    
    private Usuario usuario;
    private Rol rol1, rol2;
    private final String correo = "test@example.com";
    private final String password = "password123";
    private final String passwordEncriptada = "$2a$10$hashedPassword";

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo(correo);
        usuario.setPassword(passwordEncriptada);
        
        rol1 = new Rol();
        rol1.setId(1L);
        rol1.setNombre("Admin");
        
        rol2 = new Rol();
        rol2.setId(2L);
        rol2.setNombre("Usuario");
    }

    @Test
    void obtenerRoles_lista() {
        // Configurar
        List<Rol> rolesDesordenados = Arrays.asList(rol2, rol1);
        when(rolRepository.findAll()).thenReturn(rolesDesordenados);
        
        // Ejecutar
        List<Rol> resultado = authService.obtenerRoles();
        
        // Verificar
        assertEquals(2, resultado.size());
        assertEquals("Admin", resultado.get(0).getNombre()); // Verifica orden alfabético
        assertEquals("Usuario", resultado.get(1).getNombre());
        verify(rolRepository).findAll();
    }

    @Test
    void validarCredenciales_correo() {
        // Configurar
        when(usuarioService.buscarPorCorreo(correo)).thenReturn(Optional.of(usuario));
        when(usuarioService.verificarPassword(password, passwordEncriptada)).thenReturn(true);
        
        // Ejecutar
        Optional<Usuario> resultado = authService.validarCredenciales(correo, password);
        
        // Verificar
        assertTrue(resultado.isPresent());
        assertEquals(correo, resultado.get().getCorreo());
        verify(usuarioService).buscarPorCorreo(correo);
        verify(usuarioService).verificarPassword(password, passwordEncriptada);
    }

    @Test
    void validarCredenciales_usuario() {
        // Configurar
        when(usuarioService.buscarPorCorreo(anyString())).thenReturn(Optional.empty());
        
        // Ejecutar
        Optional<Usuario> resultado = authService.validarCredenciales("inexistente@test.com", password);
        
        // Verificar
        assertTrue(resultado.isEmpty());
        verify(usuarioService).buscarPorCorreo("inexistente@test.com");
        verify(usuarioService, never()).verificarPassword(anyString(), anyString());
    }

    @Test
    void validarCredenciales_password() {
        // Configurar
        when(usuarioService.buscarPorCorreo(correo)).thenReturn(Optional.of(usuario));
        when(usuarioService.verificarPassword(password, passwordEncriptada)).thenReturn(false);
        
        // Ejecutar
        Optional<Usuario> resultado = authService.validarCredenciales(correo, password);
        
        // Verificar
        assertTrue(resultado.isEmpty());
        verify(usuarioService).buscarPorCorreo(correo);
        verify(usuarioService).verificarPassword(password, passwordEncriptada);
    }

    @Test
    void obtenerRoles() {
        // Configurar
        when(rolRepository.findAll()).thenReturn(List.of());
        
        // Ejecutar
        List<Rol> resultado = authService.obtenerRoles();
        
        // Verificar
        assertTrue(resultado.isEmpty());
        verify(rolRepository).findAll();
    }
}