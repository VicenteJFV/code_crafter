package com.perfulandia.service.logistica.repository;

import com.perfulandia.service.logistica.model.Logistica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LogisticaRepository extends JpaRepository<Logistica, Long> {
    Optional<Logistica> findByOrderId(Long orderId);
}
