package com.perfulandia.service.logistica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidacionStockDTO {
    private String skuProducto;
    private Integer cantidad;
}
