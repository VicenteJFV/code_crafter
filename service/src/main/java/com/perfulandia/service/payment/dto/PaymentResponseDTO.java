package com.perfulandia.service.payment.dto;

public class PaymentResponseDTO {
    private Long paymentId;
    private Float monto;
    private String metodoPago;
    private String mensaje;

    public PaymentResponseDTO(Long paymentId, Float monto, String metodoPago, String mensaje) {
        this.paymentId = paymentId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.mensaje = mensaje;
    }

    // Getters y Setters

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
