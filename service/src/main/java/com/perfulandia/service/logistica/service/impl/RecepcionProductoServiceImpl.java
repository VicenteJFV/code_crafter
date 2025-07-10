package com.perfulandia.service.logistica.service.impl;

import com.perfulandia.service.logistica.dto.RecepcionProductoDTO;
import com.perfulandia.service.logistica.model.*;
import com.perfulandia.service.logistica.repository.*;
import com.perfulandia.service.logistica.service.RecepcionProductoService;
import org.springframework.stereotype.Service;

@Service
public class RecepcionProductoServiceImpl implements RecepcionProductoService {

    private final RecepcionProductoRepository recepcionRepo;
    private final BodegaRepository bodegaRepo;
    private final StockBodegaRepository stockRepo;

    public RecepcionProductoServiceImpl(RecepcionProductoRepository recepcionRepo, BodegaRepository bodegaRepo,
            StockBodegaRepository stockRepo) {
        this.recepcionRepo = recepcionRepo;
        this.bodegaRepo = bodegaRepo;
        this.stockRepo = stockRepo;
    }

    @Override
    public RecepcionProducto registrarRecepcion(RecepcionProductoDTO dto) {
        Bodega bodega = bodegaRepo.findById(dto.getBodegaId())
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));

        // Registrar recepción
        RecepcionProducto recepcion = new RecepcionProducto();
        recepcion.setSkuProducto(dto.getSkuProducto());
        recepcion.setCantidad(dto.getCantidad());
        recepcion.setOrigen(dto.getOrigen());
        recepcion.setFecha(dto.getFecha());
        recepcion.setBodega(bodega);

        recepcionRepo.save(recepcion);

        // Actualizar o crear stock
        StockBodega stock = stockRepo.findBySkuProductoAndBodegaId(dto.getSkuProducto(), dto.getBodegaId())
                .orElseGet(() -> {
                    StockBodega nuevo = new StockBodega();
                    nuevo.setSkuProducto(dto.getSkuProducto());
                    nuevo.setCantidad(0);
                    nuevo.setBodega(bodega);
                    return nuevo;
                });

        stock.setCantidad(stock.getCantidad() + dto.getCantidad());
        stockRepo.save(stock);

        return recepcion;
    }
}
