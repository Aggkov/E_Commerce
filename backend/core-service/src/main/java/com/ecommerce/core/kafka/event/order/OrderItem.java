package com.ecommerce.core.kafka.event.order;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 3875610994208707469L;
    private Integer quantity;
    private Product product;

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}
