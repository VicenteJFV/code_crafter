package com.perfulandia.service.logistica.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class RecepcionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skuProducto;
    private Integer cantidad;
    private String origen;
    private LocalDate fecha;

    @ManyToOne
    private Bodega bodega;
}
