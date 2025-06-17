package com.perfulandia.service.orden.controller;

@RestController
@RequestMapping("/api/orders")
public class orderController {
        private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.created(URI.create("/api/orders/" + createdOrder.getId()))
                .body(createdOrder);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }

    @GetMapping("/{orderNumber}")
    public Order getOrderByNumber(@PathVariable String orderNumber) {
        return orderService.getOrderByNumber(orderNumber);
    }

}
