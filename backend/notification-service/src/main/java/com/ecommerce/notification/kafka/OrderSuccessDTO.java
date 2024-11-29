package com.ecommerce.notification.kafka;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSuccessDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2348115523199186022L;
    private String orderTrackingNumber;
    private OrderInfoDTO orderInfo;
    private List<OrderItemDTO> orderItems;
    private UserDTO user;

    @Override
    public String toString() {
        return "OrderSuccessDTO{" +
                "orderTrackingNumber='" + orderTrackingNumber + '\'' +
                ", orderInfo=" + orderInfo +
                ", orderItems=" + orderItems +
                ", user=" + user +
                '}';
    }
}
