// UsuarioController.java
package usuario.auth.service.controller;

import org.springframework.web.bind.annotation.*;
import usuario.auth.service.entity.Usuario;
import usuario.auth.service.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public Usuario registrar(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    @GetMapping("/email")
    public Usuario obtenerPorEmail(@RequestParam String email) {
        return usuarioService.obtenerPorEmail(email);
    }
}
