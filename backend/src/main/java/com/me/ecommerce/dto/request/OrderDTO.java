package com.me.ecommerce.dto.request;

import com.me.ecommerce.dto.response.CustomerDTO;
import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1250338489297010688L;

    private OrderInfoDTO orderInfoDTO;

    private CustomerDTO customerDTO;

    private Set<OrderItemDTO> orderItemDTOList = new LinkedHashSet<>();
}
