package com.perfulandia.service.Inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.perfulandia.service.Inventory.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombre(String nombre);
}
