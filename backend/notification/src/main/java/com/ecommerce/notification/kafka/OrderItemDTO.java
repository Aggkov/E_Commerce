package com.ecommerce.notification.kafka;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2661790154206140080L;
    private Integer quantity;
    private ProductDTO product;

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}
