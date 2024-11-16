package com.ecommerce.notification.service;

import com.ecommerce.notification.kafka.OrderSuccessDTO;
import com.ecommerce.notification.kafka.OrderSuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @KafkaListener(topics = "order-success-topic", groupId = "notification-group")
    public void handleOrderSuccess(OrderSuccessEvent orderSuccessEvent) {
        // Cast to your DTO class if needed (e.g., OrderSuccessDTO)
//        if (orderSuccessDTO instanceof OrderSuccessDTO) {
//            OrderSuccessDTO dto = (OrderSuccessDTO) orderSuccessDTO;
            // Process the notification logic
            log.info("Received order success notification: {}", orderSuccessEvent);
            // Add logic to send an email or process further notifications
//        }
    }
}
