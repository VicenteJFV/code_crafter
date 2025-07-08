package main.java.com.perfulandia.service.order.controller;

import com.perfulandia.service.order.dto.OrderRequestDTO;
import com.perfulandia.service.order.dto.OrderResponseDTO;
import com.perfulandia.service.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponseDTO crearOrden(@RequestBody OrderRequestDTO request,
            @AuthenticationPrincipal(expression = "id") Long clienteId) {
        return orderService.crearOrden(request, clienteId);
    }

    @GetMapping("/mine")
    public List<OrderResponseDTO> misOrdenes(@AuthenticationPrincipal(expression = "id") Long clienteId) {
        return orderService.obtenerMisOrdenes(clienteId);
    }

    @GetMapping
    public List<OrderResponseDTO> todas() {
        return orderService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO porId(@PathVariable Long id) {
        return orderService.obtenerPorId(id);
    }
}
