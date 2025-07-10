package com.perfulandia.service.payment.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.perfulandia.service.order.model.Order;
import com.perfulandia.service.order.repository.OrderRepository;
import com.perfulandia.service.payment.dto.PaymentRequestDTO;
import com.perfulandia.service.payment.dto.PaymentResponseDTO;
import com.perfulandia.service.payment.model.Payment;
import com.perfulandia.service.payment.repository.PaymentRepository;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private Payment payment;
    private PaymentRequestDTO request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setId(1L);
        order.setTotal(100.0);
        order.setEstado("CREADA");
        payment = new Payment();
        payment.setId(10L);
        payment.setMonto(100.0f);
        payment.setMetodoPago("EFECTIVO");
        payment.setOrder(order);
        request = new PaymentRequestDTO();
        request.setOrderId(1L);
        request.setMonto(100.0f);
        request.setMetodoPago("EFECTIVO");
    }

    @Test
    void registrarPago_exitoso() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrder_Id(1L)).thenReturn(List.of());
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        PaymentResponseDTO response = paymentService.registrarPago(request);
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals(100.0f, response.getMonto());
        assertEquals("EFECTIVO", response.getMetodoPago());
        assertEquals("Pago registrado exitosamente", response.getMensaje());
    }

    @Test
    void registrarPago_lanzaError_siOrdenNoExiste() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.registrarPago(request));
        assertTrue(ex.getMessage().contains("no existe"));
    }

    @Test
    void registrarPago_lanzaError_siYaTienePago() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrder_Id(1L)).thenReturn(List.of(payment));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.registrarPago(request));
        assertTrue(ex.getMessage().contains("ya tiene un pago"));
    }

    @Test
    void registrarPago_lanzaError_siMontoNoCoincide() {
        order.setTotal(200.0);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrder_Id(1L)).thenReturn(List.of());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.registrarPago(request));
        assertTrue(ex.getMessage().contains("no coincide"));
    }

    @Test
    void registrarPago_lanzaError_siYaPagada() {
        order.setEstado("PAGADA");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.findByOrder_Id(1L)).thenReturn(List.of());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.registrarPago(request));
        assertTrue(ex.getMessage().contains("ya fue pagada"));
    }

    @Test
    void obtenerPagosPorOrden_devuelveLista() {
        when(paymentRepository.findByOrder_Id(1L)).thenReturn(List.of(payment));
        List<PaymentResponseDTO> result = paymentService.obtenerPagosPorOrden(1L);
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
        assertEquals(100.0f, result.get(0).getMonto());
        assertEquals("EFECTIVO", result.get(0).getMetodoPago());
        assertEquals("Pago registrado anteriormente", result.get(0).getMensaje());
    }
}
