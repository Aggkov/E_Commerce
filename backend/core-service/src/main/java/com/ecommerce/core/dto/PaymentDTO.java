package com.ecommerce.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentDTO {
    private String paypalOrderId;
    // store tracking number not as FK, regular field
    private String orderTrackingNumber;
    private String status;
    private String currencyCode;
    private String amount;
    private String createTime;
    private String updateTime;
}
