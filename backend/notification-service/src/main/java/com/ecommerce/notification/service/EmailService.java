package com.ecommerce.notification.service;

import com.ecommerce.notification.kafka.event.order.OrderSuccessEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmail(OrderSuccessEvent orderSuccessEvent) {
        // Extract user email
        String userEmail = orderSuccessEvent.getUser().getEmail();

        // Build email content
        String subject = "E_Commerce";
        String emailBody = buildHtmlEmailBody(orderSuccessEvent);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(userEmail);
            helper.setSubject(subject);
            helper.setText(emailBody, true); // true indicates the content is HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            log.info("Failed to configure email", e);
        } catch (MailException e) {
            log.info("Failed to send email", e);
        }
    }

    private String buildHtmlEmailBody(OrderSuccessEvent orderSuccessEvent) {
        StringBuilder body = new StringBuilder();

        body.append("<html>");
        body.append("<body>");
        body.append("<h1>Order Confirmation</h1>");
        body.append("<p>Hello <strong>")
                .append(orderSuccessEvent.getUser().getFirstName()).append(" ")
                .append(orderSuccessEvent.getUser().getLastName())
                .append("</strong>,</p>");
        body.append("<p>Thank you for your order! Here are the details:</p>");

        body.append("<h2>Order Tracking Number: ")
                .append(orderSuccessEvent.getOrderTrackingNumber())
                .append("</h2>");

        body.append("<h3>Items Purchased:</h3>");
        body.append("<table border='1' cellpadding='5' cellspacing='0'>");
        body.append("<tr><th>Product</th><th>Price</th><th>Quantity</th></tr>");
        orderSuccessEvent.getOrderItems().forEach(item -> {
            body.append("<tr>")
                    .append("<td>").append(item.getProduct().getName()).append("</td>")
                    .append("<td>").append("$").append(item.getProduct().getUnitPrice()).append("</td>")
                    .append("<td>").append(item.getQuantity()).append("</td>")
                    .append("</tr>");
        });
        body.append("</table>");

        body.append("<h3>Order Summary:</h3>");
        body.append("<p><strong>Total Quantity:</strong> ")
                .append(orderSuccessEvent.getOrderInfo().getTotalQuantity())
                .append("</p>");
        body.append("<p><strong>Total Price:</strong> $")
                .append(orderSuccessEvent.getOrderInfo().getTotalPrice())
                .append("</p>");

        body.append("<h3>Shipping Address:</h3>");
        body.append("<p>")
                .append(orderSuccessEvent.getUser().getShippingAddress().getStreet()).append("<br>")
                .append(orderSuccessEvent.getUser().getShippingAddress().getCity()).append("<br>")
                .append(orderSuccessEvent.getUser().getShippingAddress().getZipCode())
                .append("</p>");

        body.append("<h3>Billing Address:</h3>");
        body.append("<p>")
                .append(orderSuccessEvent.getUser().getBillingAddress().getStreet()).append("<br>")
                .append(orderSuccessEvent.getUser().getBillingAddress().getCity()).append("<br>")
                .append(orderSuccessEvent.getUser().getBillingAddress().getZipCode())
                .append("</p>");

        body.append("<p>If you have any questions, please contact our support team.</p>");
        body.append("<p>Thank you,<br>Your Company Team</p>");

        body.append("</body>");
        body.append("</html>");

        return body.toString();
    }
}
