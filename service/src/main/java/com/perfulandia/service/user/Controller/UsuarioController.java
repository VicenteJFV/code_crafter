package com.perfulandia.service.user.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Usuarios", description = "Controlador para gestionar usuarios")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios")
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Permite crear un nuevo usuario")
    public Usuario crearUsuario(@Valid @RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Permite actualizar un usuario existente")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id,
            @Valid @RequestBody Usuario usuarioActualizado) {
        Optional<Usuario> usuarioOptional = usuarioService.buscarPorId(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setCorreo(usuarioActualizado.getCorreo());
            return ResponseEntity.ok(usuarioService.guardarUsuario(usuario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Permite eliminar un usuario por su ID")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioService.existePorId(id)) {
            usuarioService.eliminarPorId(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
