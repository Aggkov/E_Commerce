package com.me.ecommerce.dto.request;

import com.me.ecommerce.dto.response.CustomerDTO;
import com.me.ecommerce.dto.response.ProductDTO;
import jakarta.persistence.Column;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1250338489297010688L;
    // order
    private Integer orderId;

    private String orderTrackingNumber;

    private BigDecimal totalPrice;

    private Integer totalQuantity;

    private String status;

    private CustomerDTO customerDTO;

    private Set<OrderItemDTO> orderItemDTOList = new LinkedHashSet<>();
}
