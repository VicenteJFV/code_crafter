package main.java.com.perfulandia.service.order.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime fechaCreacion;
    private Double total;
    private String estado;
    private List<ItemDTO> detalles;

    @Data
    @AllArgsConstructor
    public static class ItemDTO {
        private Long productoId;
        private Integer cantidad;
        private Double precioUnitario;
    }
}
