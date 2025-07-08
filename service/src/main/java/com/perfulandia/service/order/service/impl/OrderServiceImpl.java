package com.perfulandia.service.order.service.impl;

import com.perfulandia.service.order.dto.OrderRequestDTO;
import com.perfulandia.service.order.dto.OrderResponseDTO;
import com.perfulandia.service.order.model.Order;
import com.perfulandia.service.order.model.OrderDetail;
import com.perfulandia.service.order.repository.OrderRepository;
import com.perfulandia.service.order.repository.OrderDetailRepository;
import com.perfulandia.service.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository detailRepository;
    private final RestTemplate restTemplate;

    private static final String PRODUCTO_API_URL = "http://localhost:8080/api/productos/";

    @Override
    public OrderResponseDTO crearOrden(OrderRequestDTO request, Long clienteId) {
        // Validar stock y descontar en cada producto
        for (OrderRequestDTO.ItemDTO item : request.getItems()) {
            // Para GET
            HttpEntity<Void> entity = new HttpEntity<>(buildAuthHeaders());
            ResponseEntity<ProductoDTO> response = restTemplate.exchange(
                    PRODUCTO_API_URL + item.getProductoId(),
                    HttpMethod.GET,
                    entity,
                    ProductoDTO.class);
            ProductoDTO producto = response.getBody();

            if (producto == null || producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto ID: " + item.getProductoId());
            }

            // Descontar stock (llamada a PATCH o PUT del microservicio de productos)
            HttpEntity<Integer> patchEntity = new HttpEntity<>(item.getCantidad(), buildAuthHeaders());
            restTemplate.exchange(
                    PRODUCTO_API_URL + item.getProductoId() + "/descontar",
                    HttpMethod.PATCH,
                    patchEntity,
                    Void.class);
        }

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

    // DTO para consumir producto desde el microservicio externo
    @lombok.Data
    public static class ProductoDTO {
        private Long id;
        private String nombre;
        private Double precio;
        private Integer stock;
    }

    private HttpHeaders buildAuthHeaders() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = attrs.getRequest().getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        return headers;
    }
}
