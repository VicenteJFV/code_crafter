package com.perfulandia.service.logistica.service;

import com.perfulandia.service.logistica.dto.LogisticaDTO;
import com.perfulandia.service.logistica.model.Logistica;

public interface LogisticaService {
    Logistica crearSeguimiento(LogisticaDTO dto);

    Logistica obtenerPorOrden(Long orderId);

    void iniciarDespacho(Long orderId);

    void marcarComoEntregada(Long orderId);
}
