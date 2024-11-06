package com.ecommerce.payment.controller;

import com.ecommerce.payment.service.PayPalService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final PayPalService payPalService;

    @Autowired
    public PaymentController(PayPalService payPalService) {
        this.payPalService = payPalService;
    }

    @GetMapping("/verify/{paypalOrderId}")
    public ResponseEntity<Boolean> verifyPayment(@PathVariable String paypalOrderId) {
        boolean isPaymentValid = payPalService.verifyOrder(paypalOrderId);
        if (isPaymentValid) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // Return 400 if payment invalid
        }
    }

    @PostMapping("/save")
    ResponseEntity<String> savePayment(@RequestBody Map<String, String> orderTrackingAndPayPalId) {
        payPalService.savePayment(orderTrackingAndPayPalId);
        return null;
    }
}
