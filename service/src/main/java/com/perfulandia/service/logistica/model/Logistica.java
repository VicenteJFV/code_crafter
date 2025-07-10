package com.perfulandia.service.logistica.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Logistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private String estado; // PENDIENTE, EN_RUTA, ENTREGADO
    private LocalDate fechaInicio;
    private LocalDate fechaEntregaEstimada;
    private LocalDate fechaEntregaReal;

    @ManyToOne
    private RutaDespacho ruta;

    @ManyToOne
    private Bodega bodegaSalida;
}
