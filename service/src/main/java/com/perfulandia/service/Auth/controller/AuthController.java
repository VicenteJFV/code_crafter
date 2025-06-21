package com.perfulandia.service.Auth.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.service.Auth.dto.JwtResponse;
import com.perfulandia.service.Auth.dto.LoginRequest;
import com.perfulandia.service.Auth.dto.RegistroRequest;
import com.perfulandia.service.Auth.service.AuthService;
import com.perfulandia.service.Auth.service.JwtUtil;
import com.perfulandia.service.user.model.Rol;
import com.perfulandia.service.user.model.Usuario;
import com.perfulandia.service.user.repository.RolRepository;
import com.perfulandia.service.user.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Controlador de autenticación y registro de usuarios")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Permite a un usuario iniciar sesión con su correo y contraseña")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorCorreo(request.getCorreo());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (usuarioService.verificarPassword(request.getPassword(), usuario.getPassword())) {
                String token = jwtUtil.generarToken(usuario);
                return ResponseEntity.ok(new JwtResponse(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }
    }

    @PostMapping("/register")
@Operation(summary = "Registrar usuario", description = "Permite registrar un nuevo usuario en el sistema")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegistroRequest request) {
        try {
            System.out.println("Registrando usuario: " + request.getCorreo());

            if (usuarioService.buscarPorCorreo(request.getCorreo()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
            }

            Optional<Rol> rolOpt = rolRepository.findById(request.getRolId());
            if (rolOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rol inválido");
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(request.getNombre());
            nuevoUsuario.setCorreo(request.getCorreo());
            nuevoUsuario.setPassword(request.getPassword());
            nuevoUsuario.setRol(rolOpt.get());

            usuarioService.guardarUsuario(nuevoUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente");

        } catch (Exception e) {
            e.printStackTrace(); // Esto mostrará el error real en la consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping("/roles")
@Operation(summary = "Obtener roles disponibles", description = "Devuelve una lista de roles disponibles para asignar a los usuarios")
    public ResponseEntity<List<Rol>> obtenerRolesDisponibles() {
        return ResponseEntity.ok(authService.obtenerRoles());
    }

}
