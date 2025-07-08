package com.perfulandia.service.order.service;

import com.perfulandia.service.order.dto.OrderRequestDTO;
import com.perfulandia.service.order.dto.OrderResponseDTO;
import java.util.List;

public interface OrderService {
    OrderResponseDTO crearOrden(OrderRequestDTO request, Long clienteId);

    List<OrderResponseDTO> obtenerMisOrdenes(Long clienteId);

    List<OrderResponseDTO> obtenerTodas();

    OrderResponseDTO obtenerPorId(Long id);
}
