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
        if (rolRepository.count() == 0) {
            rolRepository.save(new Rol("ROLE_ADMIN"));
            rolRepository.save(new Rol("ROLE_GERENTE"));
            rolRepository.save(new Rol("ROLE_EMPLEADO"));
            rolRepository.save(new Rol("ROLE_CLIENTE"));
            System.out.println("Roles iniciales creados");
        }
    }
}
