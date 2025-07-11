package com.perfulandia.service.logistica.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.perfulandia.service.logistica.dto.LogisticaDTO;
import com.perfulandia.service.logistica.model.Logistica;
import com.perfulandia.service.logistica.repository.LogisticaRepository;

class LogisticaServiceImplTest {

    @Mock
    private LogisticaRepository logisticaRepository;
    @InjectMocks
    private LogisticaServiceImpl logisticaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearSeguimiento_creaYGuardaLogistica() {
        LogisticaDTO dto = new LogisticaDTO();
        dto.setOrderId(1L);
        Logistica logisticaMock = new Logistica();
        logisticaMock.setOrderId(1L);
        logisticaMock.setEstado("EN PREPARACION");
        when(logisticaRepository.save(any(Logistica.class))).thenReturn(logisticaMock);

        Logistica result = logisticaService.crearSeguimiento(dto);
        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals("EN PREPARACION", result.getEstado());
        verify(logisticaRepository).save(any(Logistica.class));
    }

    @Test
    void obtenerPorOrden_devuelveLogisticaSiExiste() {
        Logistica logisticaMock = new Logistica();
        logisticaMock.setOrderId(2L);
        when(logisticaRepository.findByOrderId(2L)).thenReturn(Optional.of(logisticaMock));

        Logistica result = logisticaService.obtenerPorOrden(2L);
        assertNotNull(result);
        assertEquals(2L, result.getOrderId());
    }

    @Test
    void obtenerPorOrden_lanzaExcepcionSiNoExiste() {
        when(logisticaRepository.findByOrderId(3L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> logisticaService.obtenerPorOrden(3L));
        assertTrue(ex.getMessage().contains("No se encontró seguimiento para la orden"));
    }

    @Test
    void iniciarDespacho_creaYGuardaLogisticaDespacho() {
        ArgumentCaptor<Logistica> captor = ArgumentCaptor.forClass(Logistica.class);
        when(logisticaRepository.save(any(Logistica.class))).thenReturn(new Logistica());

        logisticaService.iniciarDespacho(4L);
        verify(logisticaRepository).save(captor.capture());
        Logistica saved = captor.getValue();
        assertEquals(4L, saved.getOrderId());
        assertEquals("EN DESPACHO", saved.getEstado());
        assertNotNull(saved.getFechaEntregaEstimada());
    }

    @Test
    void marcarComoEntregada_actualizaLogisticaSiExiste() {
        Logistica logisticaMock = new Logistica();
        logisticaMock.setOrderId(5L);
        logisticaMock.setEstado("EN DESPACHO");
        when(logisticaRepository.findByOrderId(5L)).thenReturn(Optional.of(logisticaMock));
        when(logisticaRepository.save(any(Logistica.class))).thenReturn(logisticaMock);

        logisticaService.marcarComoEntregada(5L);
        assertEquals("ENTREGADO", logisticaMock.getEstado());
        assertNotNull(logisticaMock.getFechaEntregaReal());
        verify(logisticaRepository).save(logisticaMock);
    }

    @Test
    void marcarComoEntregada_lanzaExcepcionSiNoExiste() {
        when(logisticaRepository.findByOrderId(6L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> logisticaService.marcarComoEntregada(6L));
        assertTrue(ex.getMessage().contains("No se encontró una orden logística"));
    }
}
