package com.perfulandia.service.user.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.perfulandia.service.Auth.service.JwtUtil;
import com.perfulandia.service.user.config.JwtConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProtectedController.class)
class ProtectedControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtConfig jwtConfig;

    @Test
    @WithMockUser
    void bienvenida() throws Exception {
        mockMvc.perform(get("/api/protected/bienvenida"))
                .andExpect(status().isOk())
                .andExpect(content().string("¡Acceso permitido! Este mensaje está protegido con JWT."));
    }

    @Test
    @WithMockUser
    void saludoProtegido() throws Exception {
        mockMvc.perform(get("/api/protected/saludo"))
                .andExpect(status().isOk())
                .andExpect(content().string("¡Accediste a una ruta protegida!"));
    }
}