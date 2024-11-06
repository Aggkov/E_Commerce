package com.ecommerce.payment.controller;

import com.ecommerce.payment.dto.OrderIdPayPalIdDTO;
import com.ecommerce.payment.service.PayPalService;
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

    @Autowired
    private PayPalService payPalService;

    @GetMapping("/verify/{paypalOrderId}")
    public ResponseEntity<Boolean> verifyPayment(@PathVariable String paypalOrderId) {
        boolean isPaymentValid = payPalService.verifyOrder(paypalOrderId);
        if (isPaymentValid) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false); // Return 400 if payment invalid
        }
    }

    // TODO create savePayment
    // in service parse response from
    // .uri(URI.create("https://api.sandbox.paypal.com/v2/checkout/orders/" + paypalOrderId))
    // create new payment and save it to new container payment_db
    @PostMapping("/save")
    ResponseEntity<String> savePayment(@RequestBody OrderIdPayPalIdDTO orderIdPayPalIdDTO) {
        payPalService.savePayment(orderIdPayPalIdDTO);
        return null;
    }
}
