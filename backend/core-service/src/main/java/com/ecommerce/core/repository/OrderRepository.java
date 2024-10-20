package com.ecommerce.core.repository;

import com.ecommerce.core.entity.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o.orderTrackingNumber " +
            "FROM Order o " +
            "WHERE o.orderTrackingNumber = :orderTrackingNumber")
    String findOrderTrackingNumber(@Param("orderTrackingNumber") String orderTrackingNumber);
}
