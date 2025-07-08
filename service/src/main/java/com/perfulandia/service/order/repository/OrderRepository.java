package com.perfulandia.service.order.repository;

import com.perfulandia.service.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClienteId(Long clienteId);
}
