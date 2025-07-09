package com.perfulandia.service.payment.model;

import com.perfulandia.service.order.model.Order;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float monto;

    private String metodoPago;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
