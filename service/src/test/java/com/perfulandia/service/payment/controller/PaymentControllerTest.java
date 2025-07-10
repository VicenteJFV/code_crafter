package com.perfulandia.service.payment.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.service.payment.dto.PaymentRequestDTO;
import com.perfulandia.service.payment.dto.PaymentResponseDTO;
import com.perfulandia.service.payment.service.PaymentService;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentRequestDTO paymentRequest;
    private PaymentResponseDTO paymentResponse;

    @MockBean
    private com.perfulandia.service.user.config.JwtFilter jwtFilter;

    @MockBean
    private com.perfulandia.service.user.config.UserDetailsServiceImpl userDetailsServiceImpl;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequestDTO();
        
        paymentResponse = new PaymentResponseDTO();
        
    }

    @Test
    void registrarPago_retornaOk() throws Exception {
        when(paymentService.registrarPago(any(PaymentRequestDTO.class))).thenReturn(paymentResponse);

        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPagosPorOrden_retornaLista() throws Exception {
        when(paymentService.obtenerPagosPorOrden(1L)).thenReturn(List.of(paymentResponse));

        mockMvc.perform(get("/api/payment/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }
}
