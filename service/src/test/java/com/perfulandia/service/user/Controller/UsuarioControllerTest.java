package com.perfulandia.service.user.Controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.perfulandia.service.Auth.service.JwtUtil;
import com.perfulandia.service.user.config.JwtConfig;
import com.perfulandia.service.user.model.Rol;
import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;
    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtConfig jwtConfig;

    private Usuario usuario;
    private final Long id = 1L;
    private final String correo = "test@example.com";
    private final String nombre = "Test User";
    private final String password = "password123";

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(id);
        usuario.setCorreo(correo);
        usuario.setNombre(nombre);
        usuario.setPassword(password);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void obtenerUsuarios() throws Exception {
        when(usuarioService.listarUsuarios()).thenReturn(Arrays.asList(usuario));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.usuarioList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.usuarioList[0].id", is(id.intValue())))
                .andExpect(jsonPath("$._embedded.usuarioList[0].correo", is(correo)))
                .andExpect(jsonPath("$._embedded.usuarioList[0].nombre", is(nombre)));

        verify(usuarioService).listarUsuarios();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearUsuario() throws Exception {
        when(usuarioService.guardarUsuario(any(Usuario.class))).thenReturn(usuario);

        String json = String.format(
                "{\"id\":%d,\"correo\":\"%s\",\"nombre\":\"%s\",\"password\":\"%s\"}",
                id, correo, nombre, password);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.correo", is(correo)))
                .andExpect(jsonPath("$.nombre", is(nombre)));

        verify(usuarioService).guardarUsuario(any(Usuario.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizarUsuario() throws Exception {
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(id);
        usuarioActualizado.setCorreo("nuevo@mail.com");
        usuarioActualizado.setNombre("Nuevo Nombre");
        usuarioActualizado.setPassword(password);

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ROLE_ADMIN");
        usuarioActualizado.setRol(rol);
        usuario.setRol(rol);

        when(usuarioService.buscarPorId(id)).thenReturn(Optional.of(usuario));
        when(usuarioService.guardarUsuario(any(Usuario.class))).thenReturn(usuarioActualizado);
        when(usuarioService.buscarPorCorreo(anyString())).thenReturn(Optional.of(usuarioActualizado));

        String json = String.format(
                "{\"id\":%d,\"correo\":\"%s\",\"nombre\":\"%s\",\"password\":\"%s\"}",
                id, "nuevo@mail.com", "Nuevo Nombre", password);

        mockMvc.perform(put("/api/usuarios/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo", is("nuevo@mail.com")))
                .andExpect(jsonPath("$.nombre", is("Nuevo Nombre")));

        verify(usuarioService).buscarPorId(id);
        verify(usuarioService).guardarUsuario(any(Usuario.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizarUsuario_notFound() throws Exception {
        when(usuarioService.buscarPorId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/usuarios/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"X\",\"correo\":\"x@mail.com\",\"password\":\"pass\"}"))
                .andExpect(status().isNotFound());

        verify(usuarioService).buscarPorId(99L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarUsuario() throws Exception {
        when(usuarioService.existePorId(id)).thenReturn(true);
        Mockito.doNothing().when(usuarioService).eliminarPorId(id);

        mockMvc.perform(delete("/api/usuarios/{id}", id))
                .andExpect(status().isNoContent());

        verify(usuarioService).existePorId(id);
        verify(usuarioService).eliminarPorId(id);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarUsuario_notFound() throws Exception {
        when(usuarioService.existePorId(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/api/usuarios/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(usuarioService).existePorId(99L);
    }
}