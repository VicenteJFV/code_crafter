package com.perfulandia.service.logistica.repository;

import com.perfulandia.service.logistica.model.StockBodega;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StockBodegaRepository extends JpaRepository<StockBodega, Long> {
    Optional<StockBodega> findBySkuProductoAndBodegaId(String skuProducto, Long bodegaId);
}
