package com.perfulandia.service.Inventory.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.perfulandia.service.Inventory.dto.ProductoDTO;
import com.perfulandia.service.Inventory.mapper.ProductoMapper;
import com.perfulandia.service.Inventory.model.Producto;
import com.perfulandia.service.Inventory.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;
    
    @Mock
    private ProductoMapper productoMapper;
    
    @InjectMocks
    private ProductoService productoService;
    
    private Producto producto;
    private ProductoDTO productoDTO;
    private final Long id = 1L;
    private final String nombre = "Producto Test";
    private final String descripcion = "Descripción de prueba";
    private final Integer stock = 10;
    private final Double precio = 99.99;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setStock(stock);
        producto.setPrecio(precio);
        
        productoDTO = new ProductoDTO();
        productoDTO.setId(id);
        productoDTO.setNombre(nombre);
        productoDTO.setDescripcion(descripcion);
        productoDTO.setStock(stock);
        productoDTO.setPrecio(precio);
    }

    @Test
    void obtenerTodos() {
        // Configurar
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));
        when(productoMapper.toDto(producto)).thenReturn(productoDTO);
        
        // Ejecutar
        List<ProductoDTO> resultado = productoService.obtenerTodos();
        
        // Verificar
        assertEquals(1, resultado.size());
        assertEquals(id, resultado.get(0).getId());
        verify(productoRepository).findAll();
        verify(productoMapper).toDto(producto);
    }

    @Test
    void obtenerPorId() {
        // Configurar
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(productoMapper.toDto(producto)).thenReturn(productoDTO);
        
        // Ejecutar
        ProductoDTO resultado = productoService.obtenerPorId(id);
        
        // Verificar
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(productoRepository).findById(id);
        verify(productoMapper).toDto(producto);
    }


    @Test
    void crear() {
        // Configurar
        when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
        when(productoRepository.save(producto)).thenReturn(producto);
        when(productoMapper.toDto(producto)).thenReturn(productoDTO);
        
        // Ejecutar
        ProductoDTO resultado = productoService.crear(productoDTO);
        
        // Verificar
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(productoMapper).toEntity(productoDTO);
        verify(productoRepository).save(producto);
        verify(productoMapper).toDto(producto);
    }

    @Test
    void actualizar() {
        // Configurar
        ProductoDTO dtoActualizado = new ProductoDTO();
        dtoActualizado.setNombre("Nuevo nombre");
        dtoActualizado.setDescripcion("Nueva descripción");
        dtoActualizado.setStock(20);
        dtoActualizado.setPrecio(199.99);
        
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(productoRepository.save(producto)).thenReturn(producto);
        when(productoMapper.toDto(producto)).thenReturn(dtoActualizado);
        
        // Ejecutar
        ProductoDTO resultado = productoService.actualizar(id, dtoActualizado);
        
        // Verificar
        assertNotNull(resultado);
        assertEquals("Nuevo nombre", producto.getNombre());
        assertEquals("Nueva descripción", producto.getDescripcion());
        assertEquals(Integer.valueOf(20), producto.getStock());
        assertEquals(Double.valueOf(199.99), producto.getPrecio());
        verify(productoRepository).findById(id);
        verify(productoRepository).save(producto);
    }

    

    @Test
    void eliminar() {
        // Configurar
        when(productoRepository.existsById(id)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(id);
        
        // Ejecutar
        productoService.eliminar(id);
        
        // Verificar
        verify(productoRepository).existsById(id);
        verify(productoRepository).deleteById(id);
    }

   
}