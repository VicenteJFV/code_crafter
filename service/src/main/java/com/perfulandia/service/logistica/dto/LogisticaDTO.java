package com.perfulandia.service.logistica.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LogisticaDTO {
    private Long orderId;
    private String estado; // PENDIENTE, EN_RUTA, ENTREGADO
    private LocalDate fechaEntregaEstimada;
    private Long rutaId;
    private Long bodegaId;
}
