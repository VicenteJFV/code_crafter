package com.perfulandia.service.Inventory.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.perfulandia.service.Auth.service.JwtUtil;
import com.perfulandia.service.Inventory.dto.ProductoDTO;
import com.perfulandia.service.Inventory.service.ProductoService;
import com.perfulandia.service.user.config.JwtConfig;

@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtConfig jwtConfig;

    private ProductoDTO productoDTO;
    private final Long id = 1L;
    private final String nombre = "Producto Test";
    private final String descripcion = "Descripción de prueba";
    private final Integer stock = 10;
    private final Double precio = 99.99;

    @BeforeEach
    void setUp() {
        productoDTO = new ProductoDTO();
        productoDTO.setId(id);
        productoDTO.setNombre(nombre);
        productoDTO.setDescripcion(descripcion);
        productoDTO.setStock(stock);
        productoDTO.setPrecio(precio);
    }

    @Test
    @WithMockUser(roles = { "GERENTE" })
    void obtenerTodos() throws Exception {
        List<ProductoDTO> productos = Arrays.asList(productoDTO);
        when(productoService.obtenerTodos()).thenReturn(productos);

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(id.intValue())))
                .andExpect(jsonPath("$[0].nombre", is(nombre)))
                .andExpect(jsonPath("$[0].descripcion", is(descripcion)))
                .andExpect(jsonPath("$[0].stock", is(stock)))
                .andExpect(jsonPath("$[0].precio", is(precio)));

        verify(productoService).obtenerTodos();
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void obtenerPorId() throws Exception {
        when(productoService.obtenerPorId(id)).thenReturn(productoDTO);

        mockMvc.perform(get("/api/productos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.nombre", is(nombre)))
                .andExpect(jsonPath("$.descripcion", is(descripcion)))
                .andExpect(jsonPath("$.stock", is(stock)))
                .andExpect(jsonPath("$.precio", is(precio)));

        verify(productoService).obtenerPorId(id);
    }

    @Test
    @WithMockUser(roles = { "GERENTE" })
    void crear() throws Exception {
        when(productoService.crear(any(ProductoDTO.class))).thenReturn(productoDTO);

        String json = String.format(
                "{\"nombre\":\"%s\",\"descripcion\":\"%s\",\"stock\":%d,\"precio\":%.2f}",
                nombre, descripcion, stock, precio);

        mockMvc.perform(post("/api/productos/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.nombre", is(nombre)))
                .andExpect(jsonPath("$.descripcion", is(descripcion)))
                .andExpect(jsonPath("$.stock", is(stock)))
                .andExpect(jsonPath("$.precio", is(precio)));

        verify(productoService).crear(any(ProductoDTO.class));
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void actualizar() throws Exception {
        ProductoDTO dtoActualizado = new ProductoDTO();
        dtoActualizado.setId(id);
        dtoActualizado.setNombre("Nuevo nombre");
        dtoActualizado.setDescripcion("Nueva descripción");
        dtoActualizado.setStock(20);
        dtoActualizado.setPrecio(199.99);

        when(productoService.actualizar(eq(id), any(ProductoDTO.class))).thenReturn(dtoActualizado);

        String json = String.format(
                "{\"nombre\":\"%s\",\"descripcion\":\"%s\",\"stock\":%d,\"precio\":%.2f}",
                "Nuevo nombre", "Nueva descripción", 20, 199.99);

        mockMvc.perform(put("/api/productos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.nombre", is("Nuevo nombre")))
                .andExpect(jsonPath("$.descripcion", is("Nueva descripción")))
                .andExpect(jsonPath("$.stock", is(20)))
                .andExpect(jsonPath("$.precio", is(199.99)));

        verify(productoService).actualizar(eq(id), any(ProductoDTO.class));
    }

    @Test
    @WithMockUser(roles = { "GERENTE" })
    void eliminar() throws Exception {
        mockMvc.perform(delete("/api/productos/{id}", id))
                .andExpect(status().isOk());

        verify(productoService).eliminar(id);
    }
}
