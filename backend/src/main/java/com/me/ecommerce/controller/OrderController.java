package com.me.ecommerce.controller;

import com.me.ecommerce.dto.request.OrderDTO;
import com.me.ecommerce.dto.response.OrderDTOResponse;
import com.me.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/api/order", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200") // Replace with your frontend URL
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTOResponse> createOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createNewOrder(orderDTO));
    }
}
