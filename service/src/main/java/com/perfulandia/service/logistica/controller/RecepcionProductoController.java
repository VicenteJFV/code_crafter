package com.perfulandia.service.logistica.controller;

import com.perfulandia.service.logistica.dto.RecepcionProductoDTO;
import com.perfulandia.service.logistica.model.RecepcionProducto;
import com.perfulandia.service.logistica.service.RecepcionProductoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logistica/recepcion")
public class RecepcionProductoController {

    private final RecepcionProductoService recepcionService;

    public RecepcionProductoController(RecepcionProductoService recepcionService) {
        this.recepcionService = recepcionService;
    }

    @PostMapping
    public RecepcionProducto registrar(@RequestBody RecepcionProductoDTO dto) {
        return recepcionService.registrarRecepcion(dto);
    }
}
