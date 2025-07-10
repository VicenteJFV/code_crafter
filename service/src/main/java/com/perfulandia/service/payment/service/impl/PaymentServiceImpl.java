package com.perfulandia.service.payment.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.service.order.model.Order;
import com.perfulandia.service.order.repository.OrderRepository;
import com.perfulandia.service.payment.dto.PaymentRequestDTO;
import com.perfulandia.service.payment.dto.PaymentResponseDTO;
import com.perfulandia.service.payment.model.Payment;
import com.perfulandia.service.payment.repository.PaymentRepository;
import com.perfulandia.service.payment.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public PaymentResponseDTO registrarPago(PaymentRequestDTO request) {
        Optional<Order> ordenOptional = orderRepository.findById(request.getOrderId());
        if (ordenOptional.isEmpty()) {
            throw new RuntimeException("La orden con ID " + request.getOrderId() + " no existe");
        }

        Order orden = ordenOptional.get();

        // Validar si ya tiene pagos registrados
        if (!paymentRepository.findByOrder_Id(orden.getId()).isEmpty()) {
            throw new RuntimeException("La orden ya tiene un pago registrado");
        }

        // Validar si el monto coincide con el total de la orden
        if (Math.abs(request.getMonto() - orden.getTotal()) > 0.01) {
            throw new RuntimeException("El monto ingresado no coincide con el total de la orden");
        }

        // Validar si ya está pagada
        if ("PAGADA".equalsIgnoreCase(orden.getEstado())) {
            throw new RuntimeException("La orden ya fue pagada anteriormente");
        }

        Payment pago = new Payment();
        pago.setMonto(request.getMonto().floatValue());
        pago.setMetodoPago(request.getMetodoPago());
        pago.setOrder(orden);
        paymentRepository.save(pago);

        orden.setEstado("PAGADA");
        orderRepository.save(orden);

        return new PaymentResponseDTO(
                pago.getId(),
                pago.getMonto(),
                pago.getMetodoPago(),
                "Pago registrado exitosamente");
    }

    @Override
    public List<PaymentResponseDTO> obtenerPagosPorOrden(Long orderId) {
        List<Payment> pagos = paymentRepository.findByOrder_Id(orderId);
        return pagos.stream()
                .map(p -> new PaymentResponseDTO(
                        p.getId(),
                        p.getMonto(),
                        p.getMetodoPago(),
                        "Pago registrado anteriormente"))
                .collect(Collectors.toList());
    }
}
