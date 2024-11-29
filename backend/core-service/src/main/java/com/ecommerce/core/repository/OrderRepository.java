package com.ecommerce.core.repository;

import com.ecommerce.core.dto.response.OrderCreatedDTO;
import com.ecommerce.core.entity.Order;
import java.util.Optional;
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
    @Query("SELECT DISTINCT o " +
            "FROM Order o " +
            "JOIN o.user user " +
//            "JOIN FETCH o.orderItems oi " +
//            "JOIN FETCH oi.product product " +
            "JOIN FETCH o.shippingAddress order_shipping " +
            "JOIN FETCH order_shipping.state st_shipping " +
            "JOIN FETCH o.billingAddress order_billing " +
            "JOIN FETCH order_billing.state st_billing " +
            "WHERE user.email = :email")
    Page<Order> getOrdersByUserEmail(@Param("email") String email, Pageable pageable);

    Optional<Order> findByOrderTrackingNumber(String orderTrackingNumber);

    // Alternate remove join fetch with dto projection
//    @Query("""
//    SELECT DISTINCT new com.ecommerce.core.dto.response.OrderCreatedDTO(
//        o.orderTrackingNumber,
//        o.createdAt,
//        new com.ecommerce.core.dto.request.ShippingAddressDTO(
//            sa.id, sa.city, sa.street, sa.zipCode, new com.example.dto.StateDTO(stShipping.id, stShipping.name)
//        ),
//        new com.ecommerce.core.dto.request.BillingAddressDTO(
//            ba.id, ba.city, ba.street, ba.zipCode, new com.example.dto.StateDTO(stBilling.id, stBilling.name)
//        )
//    )
//    FROM Order o
//    JOIN o.shippingAddress sa
//    JOIN sa.state stShipping
//    JOIN o.billingAddress ba
//    JOIN ba.state stBilling
//    WHERE o.user.email = :email
//""")
//    Page<OrderCreatedDTO> findOrdersByUserEmail(@Param("email") String email, Pageable pageable);

/*

//        (SELECT com.ecommerce.core.dto.request.OrderItemDTO(
//            oi.quantity,
//            new com.ecommerce.core.dto.response.ProductDTO(p.name, p.unitPrice)
//        )
//        FROM OrderItem oi JOIN oi.product p WHERE oi.order.id = o.id)
 */
}
