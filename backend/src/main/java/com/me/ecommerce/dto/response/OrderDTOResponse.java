package com.me.ecommerce.dto.response;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTOResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1959889964645223660L;

    private String orderTrackingNumber;

    public OrderDTOResponse(String orderTrackingNumber) {
        this.orderTrackingNumber = orderTrackingNumber;
    }
}
