package com.me.ecommerce.dto.request;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2670344045441781677L;

    private UUID id;

//    private String orderTrackingNumber;

    private BigDecimal totalPrice;

    private Integer totalQuantity;

    private String status;
}
