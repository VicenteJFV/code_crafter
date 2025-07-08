package main.java.com.perfulandia.service.order.service.impl;

import com.perfulandia.service.order.dto.OrderRequestDTO;
import com.perfulandia.service.order.dto.OrderResponseDTO;
import com.perfulandia.service.order.model.Order;
import com.perfulandia.service.order.model.OrderDetail;
import com.perfulandia.service.order.repository.OrderRepository;
import com.perfulandia.service.order.repository.OrderDetailRepository;
import com.perfulandia.service.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository detailRepository;

    @Override
    public OrderResponseDTO crearOrden(OrderRequestDTO request, Long clienteId) {
        Order order = Order.builder()
                .clienteId(clienteId)
                .fechaCreacion(LocalDateTime.now())
                .estado("CREADA")
                .build();

        List<OrderDetail> detalles = request.getItems().stream().map(item -> OrderDetail.builder()
                .productoId(item.getProductoId())
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .order(order)
                .build()).collect(Collectors.toList());

        order.setDetalles(detalles);
        order.setTotal(detalles.stream().mapToDouble(d -> d.getPrecioUnitario() * d.getCantidad()).sum());

        orderRepository.save(order);

        return convertir(order);
    }

    @Override
    public List<OrderResponseDTO> obtenerMisOrdenes(Long clienteId) {
        return orderRepository.findByClienteId(clienteId).stream()
                .map(this::convertir)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDTO> obtenerTodas() {
        return orderRepository.findAll().stream().map(this::convertir).collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO obtenerPorId(Long id) {
        return orderRepository.findById(id).map(this::convertir).orElse(null);
    }

    private OrderResponseDTO convertir(Order o) {
        return OrderResponseDTO.builder()
                .id(o.getId())
                .clienteId(o.getClienteId())
                .fechaCreacion(o.getFechaCreacion())
                .total(o.getTotal())
                .estado(o.getEstado())
                .detalles(o.getDetalles().stream()
                        .map(d -> new OrderResponseDTO.ItemDTO(d.getProductoId(), d.getCantidad(),
                                d.getPrecioUnitario()))
                        .collect(Collectors.toList()))
                .build();
    }
}
