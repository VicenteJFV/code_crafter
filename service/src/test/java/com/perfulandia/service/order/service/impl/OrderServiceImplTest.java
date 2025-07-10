package com.perfulandia.service.order.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.perfulandia.service.order.dto.OrderRequestDTO;
import com.perfulandia.service.order.dto.OrderResponseDTO;
import com.perfulandia.service.order.model.Order;
import com.perfulandia.service.order.repository.OrderDetailRepository;
import com.perfulandia.service.order.repository.OrderRepository;

import jakarta.servlet.http.HttpServletRequest;

class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailRepository detailRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private HttpServletRequest httpServletRequest;
    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock del token en la request
        ServletRequestAttributes attrs = mock(ServletRequestAttributes.class);
        when(attrs.getRequest()).thenReturn(httpServletRequest);
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer testtoken");
        RequestContextHolder.setRequestAttributes(attrs);
    }

    @Test
    void crearOrden_ok() {
        // Arrange
        OrderRequestDTO.ItemDTO item = new OrderRequestDTO.ItemDTO();
        item.setProductoId(10L);
        item.setCantidad(2);
        item.setPrecioUnitario(100.0);
        OrderRequestDTO request = new OrderRequestDTO();
        request.setItems(Collections.singletonList(item));

        OrderServiceImpl.ProductoDTO productoDTO = new OrderServiceImpl.ProductoDTO();
        productoDTO.setId(10L);
        productoDTO.setNombre("TestProd");
        productoDTO.setPrecio(100.0);
        productoDTO.setStock(10);

        ResponseEntity<OrderServiceImpl.ProductoDTO> productoResponse = ResponseEntity.ok(productoDTO);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class),
                eq(OrderServiceImpl.ProductoDTO.class)))
                .thenReturn(productoResponse);
        when(restTemplate.exchange(contains("/descontar"), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(ResponseEntity.ok().build());
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(1L);
            return o;
        });

        // Act
        OrderResponseDTO response = orderService.crearOrden(request, 5L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(5L, response.getClienteId());
        assertEquals("CREADA", response.getEstado());
        assertEquals(200.0, response.getTotal());
        assertEquals(1, response.getDetalles().size());
        verify(orderRepository).save(any(Order.class));
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class),
                eq(OrderServiceImpl.ProductoDTO.class));
        verify(restTemplate, times(1)).exchange(contains("/descontar"), eq(HttpMethod.PATCH), any(HttpEntity.class),
                eq(Void.class));
    }

    @Test
    void crearOrden_stockInsuficiente_lanzaExcepcion() {
        OrderRequestDTO.ItemDTO item = new OrderRequestDTO.ItemDTO();
        item.setProductoId(10L);
        item.setCantidad(5);
        item.setPrecioUnitario(100.0);
        OrderRequestDTO request = new OrderRequestDTO();
        request.setItems(Collections.singletonList(item));

        OrderServiceImpl.ProductoDTO productoDTO = new OrderServiceImpl.ProductoDTO();
        productoDTO.setId(10L);
        productoDTO.setStock(2); // stock insuficiente
        ResponseEntity<OrderServiceImpl.ProductoDTO> productoResponse = ResponseEntity.ok(productoDTO);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class),
                eq(OrderServiceImpl.ProductoDTO.class)))
                .thenReturn(productoResponse);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderService.crearOrden(request, 5L));
        assertTrue(ex.getMessage().contains("Stock insuficiente"));
    }

    @Test
    void obtenerMisOrdenes_ok() {
        Order order = Order.builder()
                .id(1L)
                .clienteId(5L)
                .fechaCreacion(LocalDateTime.now())
                .estado("CREADA")
                .detalles(Collections.emptyList())
                .total(0.0)
                .build();
        when(orderRepository.findByClienteId(5L)).thenReturn(Collections.singletonList(order));
        List<OrderResponseDTO> result = orderService.obtenerMisOrdenes(5L);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void obtenerTodas_ok() {
        Order order = Order.builder()
                .id(1L)
                .clienteId(5L)
                .fechaCreacion(LocalDateTime.now())
                .estado("CREADA")
                .detalles(Collections.emptyList())
                .total(0.0)
                .build();
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
        List<OrderResponseDTO> result = orderService.obtenerTodas();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void obtenerPorId_ok() {
        Order order = Order.builder()
                .id(1L)
                .clienteId(5L)
                .fechaCreacion(LocalDateTime.now())
                .estado("CREADA")
                .detalles(Collections.emptyList())
                .total(0.0)
                .build();
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
        OrderResponseDTO result = orderService.obtenerPorId(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerPorId_null() {
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        OrderResponseDTO result = orderService.obtenerPorId(1L);
        assertNull(result);
    }
}
