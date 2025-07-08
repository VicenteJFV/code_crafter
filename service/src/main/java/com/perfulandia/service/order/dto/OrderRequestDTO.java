package com.perfulandia.service.order.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private List<ItemDTO> items;

    @Data
    public static class ItemDTO {
        private Long productoId;
        private Integer cantidad;
        private Double precioUnitario;
    }
}
