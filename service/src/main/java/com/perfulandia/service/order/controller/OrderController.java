package com.perfulandia.service.order.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.service.inventory.controller.ProductoController;
import com.perfulandia.service.order.dto.OrderRequestDTO;
import com.perfulandia.service.order.dto.OrderResponseDTO;
import com.perfulandia.service.order.service.OrderService;
import com.perfulandia.service.user.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> crearOrden(
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.perfulandia.service.user.config.CustomUserDetails userDetails,
            @RequestBody OrderRequestDTO request) {
        if (userDetails == null)
            throw new RuntimeException("No autenticado");
        String correo = userDetails.getUsername();
        Long clienteId = userDetails.getId();

        System.out.println("Llamando a orderService.crearOrden con clienteId=" + clienteId);
        EntityModel<OrderResponseDTO> dto = agregarLinks(orderService.crearOrden(request, clienteId));
        System.out.println("Orden creada correctamente");

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/mine")
    public CollectionModel<EntityModel<OrderResponseDTO>> misOrdenes(
            @AuthenticationPrincipal(expression = "id") Long clienteId) {
        List<EntityModel<OrderResponseDTO>> modelos = orderService.obtenerMisOrdenes(clienteId).stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(modelos);
    }

    @GetMapping
    public CollectionModel<EntityModel<OrderResponseDTO>> todas() {
        List<EntityModel<OrderResponseDTO>> modelos = orderService.obtenerTodas().stream()
                .map(this::agregarLinks)
                .collect(Collectors.toList());
        return CollectionModel.of(modelos);
    }

    @GetMapping("/{id}")
    public EntityModel<OrderResponseDTO> porId(@PathVariable Long id) {
        OrderResponseDTO dto = orderService.obtenerPorId(id);
        return agregarLinks(dto);
    }

    private EntityModel<OrderResponseDTO> agregarLinks(OrderResponseDTO dto) {
        EntityModel<OrderResponseDTO> model = EntityModel.of(dto);

        model.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).porId(dto.getId())).withSelfRel());

        model.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(OrderController.class).misOrdenes(dto.getClienteId()))
                .withRel("cliente-ordenes"));

        if ("CREADA".equalsIgnoreCase(dto.getEstado())) {
            model.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(OrderController.class).porId(dto.getId())).withRel("cancelar"));
        }

        for (OrderResponseDTO.ItemDTO item : dto.getDetalles()) {
            model.add(WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(ProductoController.class)
                            .obtenerProductoPorId(item.getProductoId()))
                    .withRel("producto-" + item.getProductoId()));
        }

        return model;
    }

}
