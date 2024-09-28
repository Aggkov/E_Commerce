package com.me.ecommerce.dto.request;

import com.me.ecommerce.dto.response.CustomerDTO;
import jakarta.validation.Valid;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
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

    @Valid
    private OrderInfoDTO orderInfo;

    @Valid
    private CustomerDTO customer;


    private List<OrderItemDTO> orderItemList = new ArrayList<>();
}
