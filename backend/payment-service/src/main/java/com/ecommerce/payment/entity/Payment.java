package com.ecommerce.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "payment_id", nullable = false)
    private UUID id;

    @Column(name = "paypal_order_id", nullable = false)
    private String paypalOrderId;

    @Column(name = "order_tracking_number", nullable = false)
    private String orderTrackingNumber;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "currency_code", nullable = false, length = 10)
    private String currencyCode;

    @Column(name = "amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @ColumnDefault("clock_timestamp()")
    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "update_time")
    private Instant updateTime;
}

