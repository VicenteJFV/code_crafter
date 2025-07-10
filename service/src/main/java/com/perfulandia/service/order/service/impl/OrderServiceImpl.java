package com.perfulandia.service.order.service.impl;

import com.perfulandia.service.inventory.dto.ProductoDTO;
import com.perfulandia.service.order.dto.OrderRequestDTO;
import com.perfulandia.service.order.dto.OrderResponseDTO;
import com.perfulandia.service.order.dto.ValidacionStockDTO;
import com.perfulandia.service.order.model.Order;
import com.perfulandia.service.order.model.OrderDetail;
import com.perfulandia.service.order.repository.OrderRepository;
import com.perfulandia.service.order.repository.OrderDetailRepository;
import com.perfulandia.service.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
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
    private static final String LOGISTICA_API_URL = "http://logistica-service/api/logistica/stock/validar";

    @Override
    public OrderResponseDTO crearOrden(OrderRequestDTO request, Long clienteId) {
        // ✅ Validar stock con el microservicio logístico
        List<ValidacionStockDTO> productosValidar = request.getItems().stream()
                .map(item -> new ValidacionStockDTO(item.getProductoId().toString(), item.getCantidad()))
                .collect(Collectors.toList());

        if (!validarStockEnLogistica(productosValidar)) {
            throw new RuntimeException("Stock insuficiente detectado por el módulo logístico.");
        }

        // ✅ Validar stock y descontar en microservicio de productos
        for (OrderRequestDTO.ItemDTO item : request.getItems()) {
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

            HttpEntity<Integer> patchEntity = new HttpEntity<>(item.getCantidad(), buildAuthHeaders());
            restTemplate.exchange(
                    PRODUCTO_API_URL + item.getProductoId() + "/descontar",
                    HttpMethod.PATCH,
                    patchEntity,
                    Void.class);
        }

        // ✅ Crear la orden
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

    private boolean validarStockEnLogistica(List<ValidacionStockDTO> productos) {
        HttpHeaders headers = buildAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<ValidacionStockDTO>> request = new HttpEntity<>(productos, headers);

        try {
            ResponseEntity<Boolean> response = restTemplate.postForEntity(LOGISTICA_API_URL, request, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Error al validar stock con el servicio logístico", e);
        }
    }

    private HttpHeaders buildAuthHeaders() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = attrs.getRequest().getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        return headers;
    }
}
