package com.me.ecommerce.service;

import com.me.ecommerce.dto.request.OrderDTO;
import com.me.ecommerce.dto.response.OrderDTOResponse;

public interface OrderService {
    OrderDTOResponse createOrder(OrderDTO orderDTO);
}
