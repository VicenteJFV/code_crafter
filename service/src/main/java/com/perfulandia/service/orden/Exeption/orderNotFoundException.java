package com.perfulandia.service.orden.Exeption;

public class orderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }

}
