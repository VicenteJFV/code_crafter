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
import com.perfulandia.service.payment.exception.MontoInvalidoException;
import com.perfulandia.service.payment.exception.OrdenNoExisteException;
import com.perfulandia.service.payment.exception.OrdenYaPagadaException;
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
            throw new OrdenNoExisteException("La orden con ID " + request.getOrderId() + " no existe");
        }

        Order orden = ordenOptional.get();

        if ("PAGADA".equalsIgnoreCase(orden.getEstado())) {
            throw new OrdenYaPagadaException("La orden ya fue pagada anteriormente");
        }

        float totalOrden = (float) orden.getDetalles().stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();

        if (Math.abs(request.getMonto().floatValue() - totalOrden) > 0.01f) {
            throw new MontoInvalidoException("El monto pagado no coincide con el total de la orden.");
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
