package com.perfulandia.service.user.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.service.UsuarioService;
import com.perfulandia.service.user.dto.UsuarioUpdateDTO;

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
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve todos los usuarios con enlaces HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        List<EntityModel<Usuario>> usuariosConLinks = usuarios.stream()
                .map(usuario -> EntityModel.of(usuario,
                        linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(usuario.getId())).withSelfRel(),
                        linkTo(methodOn(UsuarioController.class).actualizarUsuario(usuario.getId(), null))
                                .withRel("actualizar"),
                        linkTo(methodOn(UsuarioController.class).eliminarUsuario(usuario.getId())).withRel("eliminar")))
                .toList();

        CollectionModel<EntityModel<Usuario>> coleccion = CollectionModel.of(
                usuariosConLinks,
                linkTo(methodOn(UsuarioController.class).obtenerUsuarios()).withSelfRel());

        return ResponseEntity.ok(coleccion);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario específico con enlaces HATEOAS")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();

        EntityModel<Usuario> usuarioModel = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).actualizarUsuario(id, null)).withRel("actualizar"),
                linkTo(methodOn(UsuarioController.class).eliminarUsuario(id)).withRel("eliminar"),
                linkTo(methodOn(UsuarioController.class).obtenerUsuarios()).withRel("todos"));

        return ResponseEntity.ok(usuarioModel);
    }

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Permite crear un nuevo usuario")
    public Usuario crearUsuario(@Valid @RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Permite actualizar parcialmente un usuario existente")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE', 'EMPLEADO', 'GERENTE')")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioUpdateDTO cambios) {

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Usuario usuario = usuarioOpt.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoLogeado = authentication.getName();

        Optional<Usuario> usuarioLogeadoOpt = usuarioService.buscarPorCorreo(correoLogeado);
        if (usuarioLogeadoOpt.isEmpty())
            return ResponseEntity.status(401).body("No autenticado");

        Usuario usuarioLogeado = usuarioLogeadoOpt.get();

        if (!usuarioLogeado.getRol().getNombre().equals("ROLE_ADMIN") && !usuarioLogeado.getId().equals(id)) {
            return ResponseEntity.status(403).body("No tienes permiso para modificar este usuario");
        }

        if (cambios.getNombre() != null && !cambios.getNombre().isBlank())
            usuario.setNombre(cambios.getNombre());

        if (cambios.getCorreo() != null && !cambios.getCorreo().isBlank())
            usuario.setCorreo(cambios.getCorreo());

        if (cambios.getPassword() != null && !cambios.getPassword().isBlank())
            usuario.setPassword(cambios.getPassword());

        return ResponseEntity.ok(usuarioService.guardarUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
