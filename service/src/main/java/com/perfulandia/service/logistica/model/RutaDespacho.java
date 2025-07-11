package com.perfulandia.service.logistica.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RutaDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreRuta;
    private String chofer;
    private String vehiculo;
    private String descripcionRecorrido;
}
