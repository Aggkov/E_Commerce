package com.ecommerce.core.repository;

import com.ecommerce.core.entity.Order;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o.orderTrackingNumber " +
            "FROM Order o " +
            "WHERE o.orderTrackingNumber = :orderTrackingNumber")
    String findOrderTrackingNumber(@Param("orderTrackingNumber") String orderTrackingNumber);

    // selecting multiple fields will result in returning Object[] not Order. So need dto projection
    @Query("SELECT o " +
//            "       o.orderTrackingNumber, o.totalPrice, o.totalQuantity, o.status, " +
//                    "o.createdAt, oi.quantity, " +
//                    "product.name, product.imageUrl, product.unitPrice " +
                    "FROM Order o " +
                    "JOIN o.user user " +
                    "JOIN o.orderItems oi " +
                    "JOIN oi.product product " +
                    "JOIN o.shippingAddress order_shipping " +
                    "JOIN order_shipping.state st_shipping " +
                    "JOIN o.billingAddress order_billing " +
                    "JOIN order_billing.state st_billing " +
                    "WHERE user.email = :email")
    Page<Order> getOrdersByUserEmail(@Param("email") String email, Pageable pageable);
}
