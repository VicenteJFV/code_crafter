package com.perfulandia.service.inventory.mapper;

import com.perfulandia.service.inventory.dto.ProductoDTO;
import com.perfulandia.service.inventory.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoDTO toDto(Producto producto) {
        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .stock(producto.getStock())
                .precio(producto.getPrecio())
                .build();
    }

    public Producto toEntity(ProductoDTO dto) {
        return Producto.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .stock(dto.getStock())
                .precio(dto.getPrecio())
                .build();
    }
}
