package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.OrderIdPayPalIdDTO;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.repository.PaypalRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayPalService {

    private final PaypalRepository paypalRepository;
    @Value("${paypal.client-id}")
    private String clientId;
    @Value("${paypal.client-secret}")
    private String clientSecret;

    public boolean verifyOrder(String paypalOrderId) {
        String token = getAccessToken();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.sandbox.paypal.com/v2/checkout/orders/" + paypalOrderId))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().contains("\"status\":\"COMPLETED\"");
        } catch (Exception e) {
            log.info("An error occurred in payment {}", e.getMessage());
            return false;
        }
    }

    public void savePayment(Map<String, String> orderTrackingAndPayPalId) {
        String token = getAccessToken();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.sandbox.paypal.com/v2/checkout/orders/" + orderTrackingAndPayPalId.get("paypalOrderId")))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (Objects.nonNull(response) && response.statusCode() == 200) {

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.body());

                Payment payment = new Payment();
                payment.setPaypalOrderId(orderTrackingAndPayPalId.get("paypalOrderId"));
                payment.setOrderTrackingNumber(orderTrackingAndPayPalId.get("orderTracking"));
                payment.setStatus(jsonNode.get("status").asText());
                payment.setCurrencyCode(jsonNode.get("purchase_units").get(0).get("amount").get("currency_code").asText());
                payment.setAmount(new BigDecimal(jsonNode.get("purchase_units").get(0).get("amount").get("value").asText()));
                payment.setCreateTime(Instant.parse(jsonNode.get("create_time").asText()));
                payment.setUpdateTime(Instant.parse(jsonNode.get("update_time").asText()));
                Payment savedPayment = paypalRepository.save(payment);

                // TODO send notification (kafka)
            }
        } catch (Exception exc) {
            log.error("Failed to retrieve access token: {}", exc.getMessage());
        }
    }

    private String getAccessToken() {
        // Encode client ID and secret in Base64 for Authorization header
        String auth = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.sandbox.paypal.com/v1/oauth2/token"))
                .header("Authorization", "Basic " + auth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (Objects.nonNull(response) && response.statusCode() == 200) {

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.body());
                return jsonNode.get("access_token").asText();
            }
        } catch (Exception exc) {
            log.error("Failed to retrieve access token: {}", exc.getMessage());
            return "Failed to retrieve access token";
        }
        // should not reach here
        return "Failed to retrieve access token";
    }
}
