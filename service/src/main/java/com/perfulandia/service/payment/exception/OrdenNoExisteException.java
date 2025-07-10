package com.perfulandia.service.payment.exception;

public class OrdenNoExisteException extends RuntimeException {
    public OrdenNoExisteException(String mensaje) {
        super(mensaje);
    }
}
