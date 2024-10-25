package com.ecommerce.core.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends Audit {
    @Serial
    private static final long serialVersionUID = -9053633357085380554L;

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "order_tracking_number")
    private String orderTrackingNumber;

    @Column(name = "total_price", precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    //  owner side == side with foreign key
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "status", length = 128)
    private String status;

    /*
    When you save an Order,
    you might want the associated OrderItems
    to be saved automatically.
     */
    @OneToMany(mappedBy = "order",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id")
    private ShippingAddress shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_id")
    private BillingAddress billingAddress;

    public void add(OrderItem item) {
        if (item != null) {
            if (orderItems == null) {
                orderItems = new LinkedHashSet<>();
            }
            orderItems.add(item);
            item.setOrder(this);
        }
    }

    public void add(ShippingAddress shippingAddress) {
        if (shippingAddress != null) {
            if (shippingAddress.getOrders() == null) {
                shippingAddress.setOrders(new LinkedHashSet<>());
            }
            this.setShippingAddress(shippingAddress);
            shippingAddress.getOrders().add(this);
        }
    }

    public void add(BillingAddress billingAddress) {
        if (billingAddress != null) {
            if (billingAddress.getOrders() == null) {
                billingAddress.setOrders(new LinkedHashSet<>());
            }
            this.setBillingAddress(billingAddress);
            billingAddress.getOrders().add(this);
        }
    }
}