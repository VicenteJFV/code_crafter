package com.perfulandia.service.order.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidacionStockDTO {
    private String skuProducto;
    private Integer cantidad;
}
