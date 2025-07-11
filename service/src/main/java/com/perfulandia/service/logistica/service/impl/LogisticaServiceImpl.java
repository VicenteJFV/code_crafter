package com.perfulandia.service.logistica.service.impl;

import com.perfulandia.service.logistica.dto.LogisticaDTO;
import com.perfulandia.service.logistica.model.Logistica;
import com.perfulandia.service.logistica.repository.LogisticaRepository;
import com.perfulandia.service.logistica.service.LogisticaService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LogisticaServiceImpl implements LogisticaService {

    private final LogisticaRepository logisticaRepository;

    public LogisticaServiceImpl(LogisticaRepository logisticaRepository) {
        this.logisticaRepository = logisticaRepository;
    }

    @Override
    public Logistica crearSeguimiento(LogisticaDTO dto) {
        Logistica logistica = new Logistica();
        logistica.setOrderId(dto.getOrderId());
        logistica.setEstado("EN PREPARACION");
        logistica.setFechaInicio(LocalDateTime.now());
        return logisticaRepository.save(logistica);
    }

    @Override
    public Logistica obtenerPorOrden(Long orderId) {
        return logisticaRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("No se encontró seguimiento para la orden: " + orderId));
    }

    @Override
    public void iniciarDespacho(Long orderId) {
        System.out.println("🚚 Iniciando despacho para orden: " + orderId); // <-- Esto es clave

        Logistica logistica = new Logistica();
        logistica.setOrderId(orderId);
        logistica.setEstado("EN DESPACHO");
        logistica.setFechaInicio(LocalDateTime.now());
        logistica.setFechaEntregaEstimada(LocalDateTime.now().plusDays(2));
        logisticaRepository.save(logistica);
    }

    @Override
    public void marcarComoEntregada(Long orderId) {
        Optional<Logistica> logisticaOpt = logisticaRepository.findByOrderId(orderId);
        if (logisticaOpt.isPresent()) {
            Logistica logistica = logisticaOpt.get();
            logistica.setEstado("ENTREGADO");
            logistica.setFechaEntregaReal(LocalDateTime.now());
            logisticaRepository.save(logistica);
        } else {
            throw new RuntimeException("No se encontró una orden logística con ID: " + orderId);
        }
    }

}
