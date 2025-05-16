package com.perfulandia.service.Auth.controller;

import com.perfulandia.service.Auth.dto.JwtResponse;
import com.perfulandia.service.Auth.dto.LoginRequest;
import com.perfulandia.service.Auth.dto.RegistroRequest;
import com.perfulandia.service.Auth.service.JwtUtil;
import com.perfulandia.service.Auth.service.AuthService;
import com.perfulandia.service.model.Rol;
import com.perfulandia.service.model.Usuario;
import com.perfulandia.service.service.UsuarioService;
import com.perfulandia.service.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorCorreo(request.getCorreo());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (usuarioService.verificarPassword(request.getPassword(), usuario.getPassword())) {
                String token = jwtUtil.generarToken(usuario.getCorreo());
                return ResponseEntity.ok(new JwtResponse(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody RegistroRequest request) {
        // Verifica si el correo ya existe
        if (usuarioService.buscarPorCorreo(request.getCorreo()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }

        // Busca el rol
        Optional<Rol> rolOpt = rolRepository.findById(request.getRolId());
        if (rolOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rol inválido");
        }

        // Crear y guardar usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(request.getNombre());
        nuevoUsuario.setCorreo(request.getCorreo());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevoUsuario.setRol(rolOpt.get());

        usuarioService.guardarUsuario(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente");
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Rol>> obtenerRolesDisponibles() {
        return ResponseEntity.ok(authService.obtenerRoles());
    }

}
