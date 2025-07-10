package com.perfulandia.service.order.controller;

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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.service.order.dto.OrderRequestDTO;
import com.perfulandia.service.order.dto.OrderResponseDTO;
import com.perfulandia.service.order.service.OrderService;
import com.perfulandia.service.user.config.CustomUserDetails;
import com.perfulandia.service.user.config.JwtFilter;
import com.perfulandia.service.user.config.UserDetailsServiceImpl;
import com.perfulandia.service.user.service.UsuarioService;

import jakarta.servlet.Filter;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testName() {
        
    }

    @BeforeEach
    void setUp2() {
        
    }

    @MockBean
    private OrderService orderService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderResponseDTO sampleOrder;

    @BeforeEach
    void setUp() {
        sampleOrder = new OrderResponseDTO();
        sampleOrder.setId(1L);
        sampleOrder.setClienteId(99L);
        sampleOrder.setEstado("CREADA");
        sampleOrder.setDetalles(List.of());

        when(orderService.crearOrden(any(), eq(99L))).thenReturn(sampleOrder);
        when(orderService.obtenerMisOrdenes(99L)).thenReturn(List.of(sampleOrder));
        when(orderService.obtenerTodas()).thenReturn(List.of(sampleOrder));
        when(orderService.obtenerPorId(1L)).thenReturn(sampleOrder);
    }

    java.lang.AssertionError: Status expected:<200> but was:<403>

    @Test
    @WithMockUser(username = "admin@correo.com")
    void todasOrdenes_retornaLista() throws Exception {
        mockMvc.perform(get("/api/orders").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.orderResponseDTOList[0].id").value(1));
    }

    @Test
    @WithMockUser
    void obtenerOrdenPorId_retornaOrdenConLinks() throws Exception {
        mockMvc.perform(get("/api/orders/1").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @TestConfiguration
    static class CustomUserDetailsTestConfig {
        @Bean
        public Filter customUserDetailsFilter() {
            return (request, response, chain) -> {
                var auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && !(auth.getPrincipal() instanceof CustomUserDetails)) {
                    var customUser = new CustomUserDetails(99L, auth.getName(), "dummy", List.of());
                    var newAuth = new TestingAuthenticationToken(customUser, null, customUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(newAuth);
                }
                chain.doFilter(request, response);
            };
        }
    }

    
}
