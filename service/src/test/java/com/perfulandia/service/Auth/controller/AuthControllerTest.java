package com.perfulandia.service.Auth.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.perfulandia.service.Auth.service.AuthService;
import com.perfulandia.service.Auth.service.JwtUtil;
import com.perfulandia.service.user.config.JwtConfig;
import com.perfulandia.service.user.model.Rol;
import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.repository.RolRepository;
import com.perfulandia.service.user.service.UsuarioService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private JwtConfig jwtConfig;
    @MockBean
    private RolRepository rolRepository;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;
    @MockBean
    private AuthService authService;

    @Test
    void loginUsuarioExistente_conPasswordCorrecto() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setCorreo("correo@dominio.com");
        usuario.setPassword("hash");

        when(usuarioService.buscarPorCorreo("correo@dominio.com")).thenReturn(Optional.of(usuario));
        when(usuarioService.verificarPassword("1234", "hash")).thenReturn(true);
        when(jwtUtil.generarToken(usuario)).thenReturn("jwt_token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "correo":"correo@dominio.com",
                                "password":"1234"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt_token"));
    }

    @Test
    void registrarUsuarioNuevo() throws Exception {
        when(usuarioService.buscarPorCorreo("nuevo@correo.com")).thenReturn(Optional.empty());
        Rol rol = new Rol();
        rol.setId(1L);
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "nombre": "Nuevo",
                                "correo": "nuevo@correo.com",
                                "password": "1234",
                                "rolId": 1
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Usuario registrado correctamente")));
    }
}
