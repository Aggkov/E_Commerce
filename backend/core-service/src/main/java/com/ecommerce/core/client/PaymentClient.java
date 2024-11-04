package com.ecommerce.core.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "payment-service", url = "${payment.service.url}")
public interface PaymentClient {

    @GetMapping("/verify/{paypalOrderId}")
    ResponseEntity<Boolean> verifyPayment(@PathVariable String paypalOrderId);

}

