package com.perfulandia.service.inventory.exception;

public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
