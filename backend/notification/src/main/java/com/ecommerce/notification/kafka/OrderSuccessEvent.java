package com.ecommerce.notification.kafka;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class OrderSuccessEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 8795222511334581276L;
    private String orderTrackingNumber;
    private OrderInfoDTO orderInfo;
    private List<OrderItemDTO> orderItems;
    private UserDTO user;
}
