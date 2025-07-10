package com.perfulandia.service.logistica.controller;

import com.perfulandia.service.logistica.dto.ValidacionStockDTO;
import com.perfulandia.service.logistica.repository.StockBodegaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logistica/stock")
public class StockController {

    private final StockBodegaRepository stockRepo;

    public StockController(StockBodegaRepository stockRepo) {
        this.stockRepo = stockRepo;
    }

    @PostMapping("/validar")
    public boolean validarStock(@RequestBody List<ValidacionStockDTO> productos) {
        for (ValidacionStockDTO prod : productos) {
            int stockTotal = stockRepo.findAll().stream()
                    .filter(s -> s.getSkuProducto().equals(prod.getSkuProducto()))
                    .mapToInt(s -> s.getCantidad())
                    .sum();

            if (stockTotal < prod.getCantidad()) {
                return false; // Stock insuficiente
            }
        }
        return true;
    }
}
