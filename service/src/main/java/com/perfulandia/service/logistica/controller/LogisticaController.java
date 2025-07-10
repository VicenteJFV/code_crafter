package com.perfulandia.service.logistica.controller;

import com.perfulandia.service.logistica.dto.LogisticaDTO;
import com.perfulandia.service.logistica.model.Logistica;
import com.perfulandia.service.logistica.service.LogisticaService;
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
}
