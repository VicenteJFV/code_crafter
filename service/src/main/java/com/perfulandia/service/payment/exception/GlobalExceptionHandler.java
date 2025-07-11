package com.perfulandia.service.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrdenNoExisteException.class)
    public ResponseEntity<Map<String, Object>> handleOrdenNoExiste(OrdenNoExisteException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(OrdenYaPagadaException.class)
    public ResponseEntity<Map<String, Object>> handleOrdenYaPagada(OrdenYaPagadaException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MontoInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleMontoInvalido(MontoInvalidoException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Manejo genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado.");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String mensaje) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", mensaje);
        return ResponseEntity.status(status).body(body);
    }
}
