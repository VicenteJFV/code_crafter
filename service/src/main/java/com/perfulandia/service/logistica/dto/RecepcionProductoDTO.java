package com.perfulandia.service.logistica.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RecepcionProductoDTO {
    private String skuProducto;
    private Integer cantidad;
    private String origen;
    private LocalDate fecha;
    private Long bodegaId;
}
