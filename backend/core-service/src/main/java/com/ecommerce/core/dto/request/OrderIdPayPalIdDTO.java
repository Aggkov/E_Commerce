package com.ecommerce.core.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderIdPayPalIdDTO {
    private String orderTrackingNumber;
    private String paypalOrderId;
}
