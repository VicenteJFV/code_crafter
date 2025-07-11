package com.perfulandia.service.logistica.controller;

import com.perfulandia.service.logistica.dto.LogisticaDTO;
import com.perfulandia.service.logistica.model.Logistica;
import com.perfulandia.service.logistica.service.LogisticaService;
import com.perfulandia.service.logistica.dto.ProcesarEnvioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logistica")
public class LogisticaController {

    private final LogisticaService logisticaService;

    public LogisticaController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    @PostMapping
    public Logistica crearSeguimiento(@RequestBody LogisticaDTO dto) {
        return logisticaService.crearSeguimiento(dto);
    }

    @GetMapping("/orden/{orderId}")
    public Logistica obtenerPorOrden(@PathVariable Long orderId) {
        return logisticaService.obtenerPorOrden(orderId);
    }

    @PostMapping("/procesar-envio")
    public ResponseEntity<?> procesarEnvio(@RequestBody ProcesarEnvioDTO dto,
            @RequestHeader("Internal-Token") String internalToken) {
        if (!"MI_TOKEN_SEGURO".equals(internalToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token interno inválido");
        }

        logisticaService.iniciarDespacho(dto.getOrderId());
        return ResponseEntity.ok("Proceso de envío iniciado para la orden " + dto.getOrderId());
    }

    @PreAuthorize("hasRole('LOGISTICA') or hasRole('ADMIN')")
    @PostMapping("/marcar-entregada/{orderId}")
    public ResponseEntity<String> marcarEntregada(@PathVariable Long orderId) {
        logisticaService.marcarComoEntregada(orderId);
        return ResponseEntity.ok("Orden marcada como entregada");
    }
}
