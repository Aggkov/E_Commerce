package com.ecommerce.core.dto.response;

import com.ecommerce.core.dto.request.BillingAddressDTO;
import com.ecommerce.core.dto.request.OrderInfoDTO;
import com.ecommerce.core.dto.request.OrderItemDTO;
import com.ecommerce.core.dto.request.ShippingAddressDTO;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import lombok.Data;

@Data
public class OrderCreatedDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -5019006446736234046L;

    private String orderTrackingNumber;
    private OrderInfoDTO orderInfo;
    private Instant createdAt;
    private List<OrderItemDTO> orderItems;
    private ShippingAddressDTO shippingAddress;
    private BillingAddressDTO billingAddress;

    public OrderCreatedDTO(String orderTrackingNumber, OrderInfoDTO orderInfo, Instant createdAt,
                           ShippingAddressDTO shippingAddress, BillingAddressDTO billingAddress,
                           OrderItemDTO orderItem) {
        this.orderTrackingNumber = orderTrackingNumber;
        this.orderInfo = orderInfo;
        this.createdAt = createdAt;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.orderItems = Collections.singletonList(orderItem);  // Single item, you can populate list later
    }
}
