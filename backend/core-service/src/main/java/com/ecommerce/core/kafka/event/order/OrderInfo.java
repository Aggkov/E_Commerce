package com.ecommerce.core.kafka.event.order;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInfo implements Serializable {
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
