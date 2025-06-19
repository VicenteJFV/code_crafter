package com.perfulandia.service.orden.model;

public  enum PaymentMethod {
    CREDIT_CARD, PAYPAL, BANK_TRANSFER

}

public enum PaymentStatus {
    PENDING, COMPLETED, FAILED, REFUNDED
}

public enum OrderStatus {
    CREATED, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}
