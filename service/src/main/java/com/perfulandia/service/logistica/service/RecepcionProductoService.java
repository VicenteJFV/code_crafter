package com.perfulandia.service.logistica.service;

import com.perfulandia.service.logistica.dto.RecepcionProductoDTO;
import com.perfulandia.service.logistica.model.RecepcionProducto;

public interface RecepcionProductoService {
    RecepcionProducto registrarRecepcion(RecepcionProductoDTO dto);
}
