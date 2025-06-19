package com.perfulandia.service.Inventory.service;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import com.perfulandia.service.Inventory.dto.ProductoDTO;
import com.perfulandia.service.Inventory.exception.ProductoNotFoundException;
import com.perfulandia.service.Inventory.mapper.ProductoMapper;
import com.perfulandia.service.Inventory.model.Producto;
import com.perfulandia.service.Inventory.repository.ProductoRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ProductoServiceTest {

    @Before
    public void setup(){

    }
        
    @Test
    public void test() {
        
    }
     private final ProductoRepository productoRepository = null;
     private final ProductoMapper mapper = new ProductoMapper();

    @Test
    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Test
    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto con ID " + id + " no encontrado."));
        return mapper.toDto(producto);
    }

    @Test
    public ProductoDTO crear(ProductoDTO dto) {
        Producto producto = mapper.toEntity(dto);
        return mapper.toDto(productoRepository.save(producto));
    }

    @Test
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto con ID " + id + " no encontrado."));

        productoExistente.setNombre(dto.getNombre());
        productoExistente.setDescripcion(dto.getDescripcion());
        productoExistente.setStock(dto.getStock());
        productoExistente.setPrecio(dto.getPrecio());

        return mapper.toDto(productoRepository.save(productoExistente));
    }

    @Test
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFoundException("Producto con ID " + id + " no encontrado.");
        }
        productoRepository.deleteById(id);
    }
}
    