//archivo solo para testeo de autenticaciones JWT
package com.perfulandia.service.user.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    @GetMapping("/bienvenida")
    public String bienvenida() {
        return "¡Acceso permitido! Este mensaje está protegido con JWT.";
    }

    @GetMapping("/saludo")
    public ResponseEntity<String> saludoProtegido() {
        return ResponseEntity.ok("¡Accediste a una ruta protegida!");
    }
}
