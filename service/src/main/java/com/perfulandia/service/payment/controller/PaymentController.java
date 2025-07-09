package com.perfulandia.service.payment.controller;

import com.perfulandia.service.payment.dto.PaymentRequestDTO;
import com.perfulandia.service.payment.dto.PaymentResponseDTO;
import com.perfulandia.service.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public PaymentResponseDTO registrarPago(@RequestBody PaymentRequestDTO request) {
        return paymentService.registrarPago(request);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponseDTO>> obtenerPagosPorOrden(@PathVariable Long orderId) {
        List<PaymentResponseDTO> pagos = paymentService.obtenerPagosPorOrden(orderId);
        return ResponseEntity.ok(pagos);
    }

}
