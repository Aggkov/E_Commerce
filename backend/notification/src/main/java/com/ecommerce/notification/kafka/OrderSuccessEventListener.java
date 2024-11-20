package com.ecommerce.notification.kafka;

import com.ecommerce.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderSuccessEventListener {

    private final EmailService emailService;

    @KafkaListener(topics = "order-success-topic", groupId = "notification-group")
    public void handleOrderSuccess(OrderSuccessEvent orderSuccessEvent) {
        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getFirstName());
        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getLastName());
        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getEmail());
        log.info("Received order success notification: {}", orderSuccessEvent.getOrderTrackingNumber());
        // Add logic to send an email or process further notifications
    }
}
