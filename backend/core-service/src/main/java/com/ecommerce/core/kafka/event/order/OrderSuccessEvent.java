package com.ecommerce.core.kafka.event.order;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSuccessEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = -2487833373070762156L;
    private String orderTrackingNumber;
    private OrderInfo orderInfo;
    private List<OrderItem> orderItems;
    private User user;
}
