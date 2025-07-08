package main.java.com.perfulandia.service.order.repository;

import com.perfulandia.service.order.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
