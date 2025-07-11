package com.perfulandia.service.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perfulandia.service.inventory.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombre(String nombre);
}
