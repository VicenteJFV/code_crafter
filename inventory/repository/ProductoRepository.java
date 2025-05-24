package com.perfulandia.service.inventory.repository;

import com.perfulandia.service.inventory.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombre(String nombre);
}
