package com.perfulandia.service.payment.service;

import com.perfulandia.service.payment.dto.PaymentRequestDTO;
import com.perfulandia.service.payment.dto.PaymentResponseDTO;
import java.util.List;

public interface PaymentService {
    PaymentResponseDTO registrarPago(PaymentRequestDTO request);

    List<PaymentResponseDTO> obtenerPagosPorOrden(Long orderId);

}
