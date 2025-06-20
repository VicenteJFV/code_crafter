package com.perfulandia.service.Auth.service;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.perfulandia.service.user.config.JwtConfig;
import com.perfulandia.service.user.model.Rol;
import com.perfulandia.service.user.model.Usuario;

import io.jsonwebtoken.Claims;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private JwtUtil jwtUtil;

    private Usuario usuario;
    private final String secret = "miClaveSecretaSuperSegura1234567890miClaveSecretaSuperSegura1234567890";
    private final long expiration = 3600000; // 1 hora
    private final String correo = "test@example.com";
    private final Long id = 1L;
    private final String rolNombre = "ADMIN";

    @BeforeEach
    void setUp() {
        when(jwtConfig.getSecret()).thenReturn(secret);
        when(jwtConfig.getExpiration()).thenReturn(expiration);

        Rol rol = new Rol();
        rol.setNombre(rolNombre);

        usuario = new Usuario();
        usuario.setId(id);
        usuario.setCorreo(correo);
        usuario.setRol(rol);
    }

    @Test
    void generarToken_Valido() {
        // Act
        String token = jwtUtil.generarToken(usuario);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Verificar contenido
        Claims claims = jwtUtil.obtenerClaims(token);
        assertEquals(correo, claims.getSubject());
        assertEquals(id.toString(), claims.get("id").toString());
        assertEquals(rolNombre, claims.get("rol").toString());
    }

    @Test
    void obtenerCorreoDesdeToken() {
        // Arrange
        String token = jwtUtil.generarToken(usuario);

        // Act
        String correoObtenido = jwtUtil.obtenerCorreoDesdeToken(token);

        // Assert
        assertEquals(correo, correoObtenido);
    }

    @Test
    void validarToken_usuario() {
        // Arrange
        String token = jwtUtil.generarToken(usuario);

        // Act
        boolean valido = jwtUtil.validarToken(token);

        // Assert
        assertTrue(valido);
    }


    @Test
    void obtenerClaims() {
        // Arrange
        String token = jwtUtil.generarToken(usuario);

        // Act
        Claims claims = jwtUtil.obtenerClaims(token);

        // Assert
        assertNotNull(claims);
        assertEquals(correo, claims.getSubject());
        assertEquals(id.toString(), claims.get("id").toString());
        assertEquals(rolNombre, claims.get("rol").toString());
    }

    @Test
    void extraerIdDesdeToken() {
        // Arrange
        String token = jwtUtil.generarToken(usuario);

        // Act
        Long idObtenido = jwtUtil.extraerIdDesdeToken(token);

        // Assert
        assertEquals(id, idObtenido);
    }

    @Test
    void extraerRolDesdeToken() {
        // Arrange
        String token = jwtUtil.generarToken(usuario);

        // Act
        String rolObtenido = jwtUtil.extraerRolDesdeToken(token);

        // Assert
        assertEquals(rolNombre, rolObtenido);
    }

    @Test
    void generarToken() {
        // Arrange
        long now = System.currentTimeMillis();
        when(jwtConfig.getExpiration()).thenReturn(1000L); 

        // Act
        String token = jwtUtil.generarToken(usuario);
        Claims claims = jwtUtil.obtenerClaims(token);

        // Assert
        Date expiracion = claims.getExpiration();
        assertTrue(expiracion.getTime() > now);
        assertTrue(expiracion.getTime() <= now + 1000L);
    }

    @Test
    void validarToken() throws InterruptedException {
        // Arrange
        when(jwtConfig.getExpiration()).thenReturn(1L); 
        String token = jwtUtil.generarToken(usuario);
        Thread.sleep(10); 

        
        boolean valido = jwtUtil.validarToken(token);

        
        assertFalse(valido);
    }
}