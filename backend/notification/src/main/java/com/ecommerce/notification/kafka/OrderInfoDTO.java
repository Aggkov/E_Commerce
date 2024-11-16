package com.ecommerce.notification.kafka;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class OrderInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2422113780038892181L;
    private BigDecimal totalPrice;
    private Integer totalQuantity;
    private String status;

    @Override
    public String toString() {
        return "OrderInfoDTO{" +
                "totalPrice=" + totalPrice +
                ", totalQuantity=" + totalQuantity +
                ", status='" + status + '\'' +
                '}';
    }
}
