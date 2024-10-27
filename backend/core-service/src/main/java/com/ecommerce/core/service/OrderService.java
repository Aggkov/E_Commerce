package com.ecommerce.core.service;

import com.ecommerce.core.dto.request.OrderDTO;
import com.ecommerce.core.dto.response.OrderCreatedDTO;
import com.ecommerce.core.dto.response.OrderSuccessDTO;
import com.ecommerce.core.dto.response.PagedResponse;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderSuccessDTO createNewOrder(OrderDTO orderDTO);
    PagedResponse<OrderCreatedDTO> getOrdersByUser(int page, int size, Authentication authentication);
    OrderCreatedDTO getOrderByTrackingNumber(String orderTrackingNumber, Authentication authentication);
}
