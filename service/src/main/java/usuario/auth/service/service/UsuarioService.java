// UsuarioService.java
package usuario.auth.service.service;

import usuario.auth.service.entity.Usuario;

public interface UsuarioService {
    Usuario registrarUsuario(Usuario usuario);

    Usuario obtenerPorEmail(String email);
}
