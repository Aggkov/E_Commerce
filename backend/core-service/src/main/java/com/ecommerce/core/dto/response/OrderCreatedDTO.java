package com.ecommerce.core.dto.response;

import com.ecommerce.core.dto.request.BillingAddressDTO;
import com.ecommerce.core.dto.request.OrderInfoDTO;
import com.ecommerce.core.dto.request.OrderItemDTO;
import com.ecommerce.core.dto.request.ShippingAddressDTO;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreatedDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -5019006446736234046L;

    private String orderTrackingNumber;
    private OrderInfoDTO orderInfo;
    private Instant createdAt;
    private List<OrderItemDTO> orderItems;
    private ShippingAddressDTO shippingAddress;
    private BillingAddressDTO billingAddress;
}
