package com.perfulandia.service.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perfulandia.service.user.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
}
