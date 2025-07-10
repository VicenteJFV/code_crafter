package com.perfulandia.service.logistica.service.impl;

import com.perfulandia.service.logistica.dto.LogisticaDTO;
import com.perfulandia.service.logistica.model.*;
import com.perfulandia.service.logistica.repository.*;
import com.perfulandia.service.logistica.service.LogisticaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LogisticaServiceImpl implements LogisticaService {

    private final LogisticaRepository logisticaRepo;
    private final BodegaRepository bodegaRepo;
    private final RutaDespachoRepository rutaRepo;

    public LogisticaServiceImpl(LogisticaRepository logisticaRepo, BodegaRepository bodegaRepo,
            RutaDespachoRepository rutaRepo) {
        this.logisticaRepo = logisticaRepo;
        this.bodegaRepo = bodegaRepo;
        this.rutaRepo = rutaRepo;
    }

    @Override
    public Logistica crearSeguimiento(LogisticaDTO dto) {
        Logistica log = new Logistica();
        log.setOrderId(dto.getOrderId());
        log.setEstado(dto.getEstado());
        log.setFechaInicio(LocalDate.now());
        log.setFechaEntregaEstimada(dto.getFechaEntregaEstimada());

        Bodega bodega = bodegaRepo.findById(dto.getBodegaId())
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        RutaDespacho ruta = rutaRepo.findById(dto.getRutaId())
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));

        log.setBodegaSalida(bodega);
        log.setRuta(ruta);

        return logisticaRepo.save(log);
    }

    @Override
    public Logistica obtenerPorOrden(Long orderId) {
        return logisticaRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Logística no encontrada para la orden: " + orderId));
    }
}
