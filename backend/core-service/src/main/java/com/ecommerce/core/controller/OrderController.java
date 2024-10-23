package com.ecommerce.core.controller;

import com.ecommerce.core.dto.response.OrderCreatedDTO;
import com.ecommerce.core.dto.response.OrderSuccessDTO;
import com.ecommerce.core.dto.request.OrderDTO;
import com.ecommerce.core.dto.response.PagedResponse;
import com.ecommerce.core.service.OrderService;
import com.ecommerce.core.utils.AppConstants;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="order", produces = MediaType.APPLICATION_JSON_VALUE)
//@CrossOrigin(origins = "http://localhost:4200") // Replace with your frontend URL
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderSuccessDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createNewOrder(orderDTO));
    }

    /*get orders by user return ordersByUserDTO
    authentication param, hasrole admin or user
    service:
        check auth get user by email or throw exception
        call order repo with user id param
        map to OrderCreatedDTO
    repo:
        query orders made by this user, return List of orders
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    @GetMapping()
    PagedResponse<OrderCreatedDTO> getOrdersByUser(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            Authentication authentication) {
        return orderService.getOrdersByUser(page ,size, authentication);
    }
}
