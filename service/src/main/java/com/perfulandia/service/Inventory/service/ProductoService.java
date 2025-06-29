package com.perfulandia.service.inventory.service;

import com.perfulandia.service.inventory.dto.ProductoDTO;
import com.perfulandia.service.inventory.exception.ProductoNotFoundException;
import com.perfulandia.service.inventory.mapper.ProductoMapper;
import com.perfulandia.service.inventory.model.Producto;
import com.perfulandia.service.inventory.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper mapper;

    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto con ID " + id + " no encontrado."));
        return mapper.toDto(producto);
    }

    public ProductoDTO crear(ProductoDTO dto) {
        Producto producto = mapper.toEntity(dto);
        return mapper.toDto(productoRepository.save(producto));
    }

    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto con ID " + id + " no encontrado."));

        productoExistente.setNombre(dto.getNombre());
        productoExistente.setDescripcion(dto.getDescripcion());
        productoExistente.setStock(dto.getStock());
        productoExistente.setPrecio(dto.getPrecio());

        return mapper.toDto(productoRepository.save(productoExistente));
    }

    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFoundException("Producto con ID " + id + " no encontrado.");
        }
        productoRepository.deleteById(id);
    }
}
