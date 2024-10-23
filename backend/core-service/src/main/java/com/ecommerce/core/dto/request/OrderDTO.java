package com.ecommerce.core.dto.request;

import com.ecommerce.core.dto.response.UserDTO;
import jakarta.validation.Valid;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1250338489297010688L;

    @Valid
    private OrderInfoDTO orderInfo;

    private List<OrderItemDTO> orderItems;

    @Valid
    private UserDTO user;
}
