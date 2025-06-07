package com.perfulandia.service.Inventory.repository;

import com.perfulandia.service.Inventory.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombre(String nombre);
}
