package com.perfulandia.service.Inventory.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.service.Inventory.dto.ProductoDTO;
import com.perfulandia.service.Inventory.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Controlador para gestionar productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos")
    public List<ProductoDTO> obtenerTodos() {
        return productoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto específico por su ID")
    @PreAuthorize("hasAnyRole('GERENTE', 'EMPLEADO', 'ADMIN')")
    public ProductoDTO obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping("/crear")
    @Operation(summary = "Crear producto", description = "Permite crear un nuevo producto")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ProductoDTO crear(@RequestBody ProductoDTO dto) {
        return productoService.crear(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Permite actualizar un producto existente")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public ProductoDTO actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        return productoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Permite eliminar un producto por su ID")
    @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
    public void eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
    }
}
