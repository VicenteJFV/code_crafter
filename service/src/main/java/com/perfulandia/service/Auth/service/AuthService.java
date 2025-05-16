package com.perfulandia.service.Auth.service;

import com.perfulandia.service.model.Rol;
import com.perfulandia.service.repository.RolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final RolRepository rolRepository;

    public AuthService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> obtenerRoles() {
        // Devuelve los roles ordenados por nombre (opcional)
        return rolRepository.findAll()
                .stream()
                .sorted((r1, r2) -> r1.getNombre().compareToIgnoreCase(r2.getNombre()))
                .toList();
    }
}
