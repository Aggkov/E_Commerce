package com.ecommerce.notification.kafka.event.order;

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
        log.info("Received order success notification: {}", orderSuccessEvent.getOrderTrackingNumber());
//        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getEmail()); // email to send to
//        orderSuccessEvent.getOrderItems().forEach((item) -> {
//            log.info(item.getProduct().getName()); // product name
//            log.info(String.valueOf(item.getProduct().getUnitPrice())); // price
//            log.info(String.valueOf(item.getQuantity())); // quantity bought
//        });
//        // totals
//        log.info("Received order success notification: {}", orderSuccessEvent.getOrderInfo().getTotalPrice());
//        log.info("Received order success notification: {}", orderSuccessEvent.getOrderInfo().getTotalQuantity());
//        // user
        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getFirstName());
        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getLastName());
        // shipping
        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getShippingAddress().getStreet());
        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getShippingAddress().getCity());
        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getShippingAddress().getZipCode());
//        // billing
//        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getBillingAddress().getStreet());
//        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getBillingAddress().getCity());
//        log.info("Received order success notification: {}", orderSuccessEvent.getUser().getBillingAddress().getZipCode());

        // send email emailService
        emailService.sendEmail(orderSuccessEvent);
    }
}
