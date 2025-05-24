package com.perfulandia.service.inventory.controller;

import com.perfulandia.service.inventory.dto.ProductoDTO;
import com.perfulandia.service.inventory.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public List<ProductoDTO> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public ProductoDTO obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ProductoDTO crear(@RequestBody ProductoDTO dto) {
        return productoService.crear(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ProductoDTO actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        return productoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public void eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
    }
}
