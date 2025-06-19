package com.perfulandia.service.orden.model;

import jakarta.persistence.Entity;

@Entity
@Table(name = "order_items")
public class OrdeItem {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long productId;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private Double priceAtPurchase;

    

}
