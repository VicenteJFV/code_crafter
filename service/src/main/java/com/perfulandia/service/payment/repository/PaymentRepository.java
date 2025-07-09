package com.perfulandia.service.payment.repository;

import com.perfulandia.service.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrderId(Long orderId);

    List<Payment> findByOrder_Id(Long orderId);

}
