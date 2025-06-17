package com.perfulandia.service.Inventory.exception;

public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
