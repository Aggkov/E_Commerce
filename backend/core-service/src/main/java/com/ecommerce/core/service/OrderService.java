package com.ecommerce.core.service;

import com.ecommerce.core.dto.request.OrderDTO;
import com.ecommerce.core.dto.response.OrderDTOResponse;

public interface OrderService {
    OrderDTOResponse createNewOrder(OrderDTO orderDTO);
}
