package com.ecommerce.core.service.impl;

import com.ecommerce.core.client.PaymentClient;
import com.ecommerce.core.dto.request.OrderDTO;
import com.ecommerce.core.dto.response.OrderCreatedDTO;
import com.ecommerce.core.dto.response.OrderSuccessDTO;
import com.ecommerce.core.dto.response.PagedResponse;
import com.ecommerce.core.entity.BillingAddress;
import com.ecommerce.core.entity.Order;
import com.ecommerce.core.entity.OrderItem;
import com.ecommerce.core.entity.Product;
import com.ecommerce.core.entity.ShippingAddress;
import com.ecommerce.core.entity.User;
import com.ecommerce.core.exception.BadRequestException;
import com.ecommerce.core.exception.PaymentVerificationException;
import com.ecommerce.core.exception.ResourceNotFoundException;
import com.ecommerce.core.mapper.OrderMapper;
import com.ecommerce.core.repository.BillingAddressRepository;
import com.ecommerce.core.repository.ShippingAddressRepository;
import com.ecommerce.core.repository.OrderRepository;
import com.ecommerce.core.repository.ProductRepository;
import com.ecommerce.core.service.OrderService;
import com.ecommerce.core.service.UserService;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


import static com.ecommerce.core.utils.AppConstants.CREATED_AT;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final AddressServiceImpl addressService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;
    private final ShippingAddressRepository shippingAddressRepository;
    private final BillingAddressRepository billingAddressRepository;
    private final PaymentClient paymentClient;

    @Override
    @Transactional
    public OrderSuccessDTO createNewOrder(OrderDTO orderDTO) {
        String paypalOrderId = orderDTO.getPaypalOrderId();
        ResponseEntity<Boolean> response = paymentClient.verifyPayment(paypalOrderId);
        if (!response.getStatusCode().is2xxSuccessful() || Boolean.FALSE.equals(response.getBody())) {
            throw new PaymentVerificationException("Payment verification failed");
        }

        Order order = orderMapper.orderDTOtoOrder(orderDTO);
        String orderTrackingNumber;
        do {
            orderTrackingNumber = generateOrderTrackingNumber();
        } while (orderRepository.findOrderTrackingNumber(orderTrackingNumber) != null);
        order.setOrderTrackingNumber(orderTrackingNumber);

        User user = userService.getCurrentUser(orderDTO.getUser());

        ShippingAddress shippingAddress = addressService.getShippingAddress(orderDTO);
        BillingAddress billingAddress = addressService.getBillingAddress(orderDTO);

        user.getShippingAddresses().add(shippingAddress);
        user.getBillingAddresses().add(billingAddress);
        shippingAddress.getUsers().add(user);
        billingAddress.getUsers().add(user);
        shippingAddressRepository.save(shippingAddress);
        billingAddressRepository.save(billingAddress);
        order.add(shippingAddress);
        order.add(billingAddress);

        // 5. Batch fetch products and map them to their IDs, find products requested by client dto
        Set<UUID> productIds = orderDTO.getOrderItems().stream()
                .map(orderItemDTO -> orderItemDTO.getProduct().getId())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<UUID, Product> productMap = productRepository.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
        // prepare orderItem
        orderDTO.getOrderItems().forEach(orderItemDTO -> {
            if (productMap.containsKey(orderItemDTO.getProduct().getId())) {
                Product product = productMap.get(orderItemDTO.getProduct().getId());
                if (Objects.isNull(product)) {
                    throw new ResourceNotFoundException("Product not found: " + orderItemDTO.getProduct().getId(), HttpStatus.NOT_FOUND);
                }
                int currentUnitsInStock = product.getUnitsInStock();
                if (currentUnitsInStock > 0) {
                    product.setUnitsInStock(--currentUnitsInStock);
                    int unitsSold = product.getUnitsSold();
                    product.setUnitsSold(++unitsSold);
                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(orderItemDTO.getQuantity());

                    product.addOrderItem(orderItem);
                    order.add(orderItem);
                } else {
                    throw new BadRequestException("Product does not have sufficient units to sell", HttpStatus.BAD_REQUEST);
                }
            }
        });

        user.add(order);
        orderRepository.save(order);

        Map<String,String> orderTrackingAndPayPalId = new HashMap<>();
        orderTrackingAndPayPalId.put("orderTracking", order.getOrderTrackingNumber());
        orderTrackingAndPayPalId.put("paypalOrderId", paypalOrderId);
        paymentClient.savePayment(orderTrackingAndPayPalId);

        OrderSuccessDTO orderSuccessDTO = orderMapper.orderToOrderSuccessDTO(order);
        return orderSuccessDTO;
    }

    @Override
    public PagedResponse<OrderCreatedDTO> getOrdersByUser(int page, int size, Authentication authentication) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
        // Extract user email from JWT token if available
        String email = Optional.ofNullable(authentication)
                .filter(auth -> auth.getPrincipal() instanceof Jwt)
                .map(auth -> ((Jwt) auth.getPrincipal()))
                .map(principal -> (String) principal.getClaim("email"))
                .orElseThrow(() -> new UsernameNotFoundException("Authentication is missing or invalid"));

        // Fetch user and verify their existence in one step
//        User registeredUser = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // find orders made by current user
        Page<Order> orderPage = orderRepository.getOrdersByUserEmail(email, pageable);

        List<OrderCreatedDTO> orderCreatedDTOs = orderPage.getContent().stream()
                    .map(orderMapper::orderToOrderCreatedDTO)
                    .toList();

        return new PagedResponse<>(
                orderCreatedDTOs,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages()
        );
    }

    @Override
    public OrderCreatedDTO getOrderByTrackingNumber(String orderTrackingNumber, Authentication authentication) {
        return orderRepository.findByOrderTrackingNumber(orderTrackingNumber)
                .map(orderMapper::orderToOrderCreatedDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order with orderTrackingNumber " + orderTrackingNumber + " was not found", HttpStatus.NOT_FOUND));
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
