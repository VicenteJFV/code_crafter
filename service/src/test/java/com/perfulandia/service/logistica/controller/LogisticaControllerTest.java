package com.perfulandia.service.logistica.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.service.logistica.dto.LogisticaDTO;
import com.perfulandia.service.logistica.dto.ProcesarEnvioDTO;
import com.perfulandia.service.logistica.model.Logistica;
import com.perfulandia.service.logistica.service.LogisticaService;

@WebMvcTest(LogisticaController.class)
@TestConfiguration
@AutoConfigureMockMvc(addFilters = false)
public class LogisticaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogisticaService logisticaService;
    
    @Autowired
    private ObjectMapper objectMapper;

    private LogisticaDTO logisticaDTO;
    private Logistica logistica;
    private ProcesarEnvioDTO procesarEnvioDTO;

    @BeforeEach
    void setUp() {
        logisticaDTO = new LogisticaDTO();
        logisticaDTO.setOrderId(1L);
        logisticaDTO.setEstado("PENDIENTE");

        logistica = new Logistica();
        logistica.setId(1L);
        logistica.setOrderId(1L);
        logistica.setEstado("PENDIENTE");

        procesarEnvioDTO = new ProcesarEnvioDTO();
        procesarEnvioDTO.setOrderId(1L);
    }

    @Test
    void crearSeguimiento() throws Exception {
        when(logisticaService.crearSeguimiento(any(LogisticaDTO.class))).thenReturn(logistica);

        mockMvc.perform(post("/api/logistica")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(logisticaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        verify(logisticaService).crearSeguimiento(any(LogisticaDTO.class));
    }

    @Test
    void obtenerPorOrden() throws Exception {
        when(logisticaService.obtenerPorOrden(1L)).thenReturn(logistica);

        mockMvc.perform(get("/api/logistica/orden/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1L));

        verify(logisticaService).obtenerPorOrden(1L);
    }

    @Test
    void procesarEnvio() throws Exception {
        mockMvc.perform(post("/api/logistica/procesar-envio")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Internal-Token", "MI_TOKEN_SEGURO")
                .content(objectMapper.writeValueAsString(procesarEnvioDTO)))
                .andExpect(status().isOk());

        verify(logisticaService).iniciarDespacho(1L);
    }

    @Test
    void procesarEnvio_auth() throws Exception {
        mockMvc.perform(post("/api/logistica/procesar-envio")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Internal-Token", "TOKEN_INVALIDO")
                .content(objectMapper.writeValueAsString(procesarEnvioDTO)))
                .andExpect(status().isForbidden());

        verify(logisticaService, never()).iniciarDespacho(any(Long.class));
    }

    @Test
    void marcarEntregada() throws Exception {
        mockMvc.perform(post("/api/logistica/marcar-entregada/1")
                .with(user("testUser").roles("LOGISTICA")))
                .andExpect(status().isOk());

        verify(logisticaService).marcarComoEntregada(1L);
    }

    
}
