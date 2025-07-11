package com.perfulandia.service.user.config;

import com.perfulandia.service.user.model.Rol;
import com.perfulandia.service.user.repository.RolRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RolRepository rolRepository;

    public DataInitializer(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @PostConstruct
    public void init() {
        crearRolSiNoExiste("ROLE_ADMIN");
        crearRolSiNoExiste("ROLE_GERENTE");
        crearRolSiNoExiste("ROLE_EMPLEADO");
        crearRolSiNoExiste("ROLE_CLIENTE");
        crearRolSiNoExiste("ROLE_LOGISTICA");
    }

    private void crearRolSiNoExiste(String nombreRol) {
        if (!rolRepository.existsByNombre(nombreRol)) {
            rolRepository.save(new Rol(nombreRol));
            System.out.println("Rol creado: " + nombreRol);
        }
    }
}
