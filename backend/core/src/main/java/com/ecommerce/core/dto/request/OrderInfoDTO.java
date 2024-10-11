package com.ecommerce.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2670344045441781677L;

    private BigDecimal totalPrice;

    private Integer totalQuantity;

    @NotBlank(message = "Status is required")
    private String status;
}
