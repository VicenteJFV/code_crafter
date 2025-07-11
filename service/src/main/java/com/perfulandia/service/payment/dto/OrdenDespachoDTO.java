package com.perfulandia.service.payment.dto;

public class OrdenDespachoDTO {
    private Long orderId;

    public OrdenDespachoDTO() {
    }

    public OrdenDespachoDTO(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
