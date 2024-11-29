package com.ecommerce.core.repository;

import com.ecommerce.core.entity.OrderItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    @Query("SELECT oi " +
            "FROM OrderItem oi " +
            "JOIN FETCH Product p " +
            "WHERE oi.order.orderTrackingNumber = :orderTrackingNumber")
    List<OrderItem> findOrderItemsByOrderTrackingNumber(@Param("orderTrackingNumber") String orderTrackingNumber);
}
