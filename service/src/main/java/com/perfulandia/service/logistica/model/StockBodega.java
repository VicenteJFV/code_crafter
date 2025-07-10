package com.perfulandia.service.logistica.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StockBodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skuProducto;
    private Integer cantidad;

    @ManyToOne
    private Bodega bodega;
}
