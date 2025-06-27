package com.perfulandia.service.Inventory.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
    @Operation(summary = "Listar productos")
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> listarProductos() {
        List<ProductoDTO> productos = productoService.obtenerTodos();

        List<EntityModel<ProductoDTO>> productosConLinks = productos.stream()
                .map(producto -> {
                    EntityModel<ProductoDTO> model = EntityModel.of(producto);
                    model.add(linkTo(methodOn(ProductoController.class)
                            .obtenerProductoPorId(producto.getId())).withSelfRel());
                    model.add(linkTo(methodOn(ProductoController.class)
                            .actualizar(producto.getId(), null)).withRel("actualizar"));
                    model.add(linkTo(methodOn(ProductoController.class)
                            .eliminar(producto.getId())).withRel("eliminar"));
                    return model;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ProductoDTO>> modelo = CollectionModel.of(productosConLinks,
                linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());

        return ResponseEntity.ok(modelo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto específico con enlaces HATEOAS")
    public ResponseEntity<EntityModel<ProductoDTO>> obtenerProductoPorId(@PathVariable Long id) {
        ProductoDTO producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<ProductoDTO> productoModel = EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerProductoPorId(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).actualizar(id, null)).withRel("actualizar"),
                linkTo(ProductoController.class).slash(id).withRel("eliminar"),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("todos"));

        return ResponseEntity.ok(productoModel);
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
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
