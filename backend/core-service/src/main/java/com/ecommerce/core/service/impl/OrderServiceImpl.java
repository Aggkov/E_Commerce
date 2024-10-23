package com.ecommerce.core.service.impl;

import com.ecommerce.core.dto.request.BillingAddressDTO;
import com.ecommerce.core.dto.request.OrderDTO;
import com.ecommerce.core.dto.request.ShippingAddressDTO;
import com.ecommerce.core.dto.response.OrderCreatedDTO;
import com.ecommerce.core.dto.response.OrderSuccessDTO;
import com.ecommerce.core.dto.response.PagedResponse;
import com.ecommerce.core.dto.response.ProductDTO;
import com.ecommerce.core.entity.BillingAddress;
import com.ecommerce.core.entity.Order;
import com.ecommerce.core.entity.OrderItem;
import com.ecommerce.core.entity.Product;
import com.ecommerce.core.entity.ShippingAddress;
import com.ecommerce.core.entity.User;
import com.ecommerce.core.exception.ResourceNotFoundException;
import com.ecommerce.core.mapper.BillingAddressMapper;
import com.ecommerce.core.mapper.OrderItemMapper;
import com.ecommerce.core.mapper.OrderMapper;
import com.ecommerce.core.mapper.ShippingAddressMapper;
import com.ecommerce.core.mapper.UserMapper;
import com.ecommerce.core.repository.BillingAddressRepository;
import com.ecommerce.core.repository.ShippingAddressRepository;
import com.ecommerce.core.repository.UserRepository;
import com.ecommerce.core.repository.OrderRepository;
import com.ecommerce.core.repository.ProductRepository;
import com.ecommerce.core.service.OrderService;
import com.ecommerce.core.service.UserService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


import static com.ecommerce.core.utils.AppConstants.CREATED_AT;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final AddressServiceImpl addressService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ShippingAddressRepository shippingAddressRepository;
    private final BillingAddressRepository billingAddressRepository;
    private final ShippingAddressMapper shippingAddressMapper;
    private final BillingAddressMapper billingAddressMapper;

    @Override
    @Transactional
    public OrderSuccessDTO createNewOrder(OrderDTO orderDTO) {
        // 1. Map OrderDTO to Order entity
        Order order = orderMapper.orderDTOtoOrder(orderDTO);

        // 2. Generate a unique order tracking number
        String orderTrackingNumber;
        do {
            orderTrackingNumber = generateOrderTrackingNumber();
        } while (orderRepository.findOrderTrackingNumber(orderTrackingNumber) != null);
        order.setOrderTrackingNumber(orderTrackingNumber);

        // if authentication, then user is registered
        // if he is in db grab him
        // else add him
        // if not authentication, user is not registered
        // if he is in db grab him
        // else add him
        User user = userService.getCurrentUser(orderDTO.getUser());

        // 4. Get or create shipping and billing addresses
        ShippingAddress shippingAddress = addressService.getShippingAddress(orderDTO);
        BillingAddress billingAddress = addressService.getBillingAddress(orderDTO);

        // TODO combine all in user entity
        user.getShippingAddresses().add(shippingAddress);
        user.getBillingAddresses().add(billingAddress);
        shippingAddress.getUsers().add(user);
        billingAddress.getUsers().add(user);
        shippingAddressRepository.save(shippingAddress);
        billingAddressRepository.save(billingAddress);

        // 5. Batch fetch products and map them to their IDs, find products requested by client dto
        Set<UUID> productIds = orderDTO.getOrderItems().stream()
                .map(orderItemDTO -> orderItemDTO.getProduct().getId())
                .collect(Collectors.toSet());

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
                // TODO decrement if units > 0 else BAD_REQUEST EXCEPTION
                int currentUnitsInStock = product.getUnitsInStock();
                product.setUnitsInStock(--currentUnitsInStock);
                int unitsSold = product.getUnitsSold();
                product.setUnitsSold(++unitsSold);
                OrderItem orderItem = orderItemMapper.orderItemDTOToOrderItem(orderItemDTO);
                // parent add child
                product.addOrderItem(orderItem);
                // parent add child
                order.add(orderItem);
            }
        });
        // TODO what if user exists in db?

        // parent add child
        user.add(order);
        // cascade persist
//        userRepository.save(user);
        orderRepository.save(order);

        OrderSuccessDTO orderSuccessDTO = orderMapper.orderDTOToOrderSuccessDTO(orderDTO);
        orderSuccessDTO.setOrderTrackingNumber(orderTrackingNumber);
        return orderSuccessDTO;
    }

    @Override
    public PagedResponse<OrderCreatedDTO> getOrdersByUser(int page, int size, Authentication authentication) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
        // Extract user email from JWT token if available
        String email = (String) Optional.ofNullable(authentication)
                .filter(auth -> auth.getPrincipal() instanceof Jwt)
                .map(auth -> ((Jwt) auth.getPrincipal()).getClaim("email"))
                .orElseThrow(() -> new UsernameNotFoundException("Authentication is missing or invalid"));

        // Fetch user and verify their existence in one step
        User registeredUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // find orders made by current user
        Page<Order> orderPage = orderRepository.getOrdersByUserEmail(email, pageable);//        // for each of these orders find their addresses and set it to current order

//        ShippingAddress shippingAddress = shippingAddressRepository.getShippingAddress(email)
//                .orElseThrow(() -> new ResourceNotFoundException("Shipping Address for this user not found", HttpStatus.NOT_FOUND));
//        BillingAddress billingAddress = billingAddressRepository.getBillingAddress(email)
//                .orElseThrow(() -> new ResourceNotFoundException("Billing Address for this user not found", HttpStatus.NOT_FOUND));
//        // map addresses later to DTO
//        ShippingAddressDTO shippingAddressDTO = shippingAddressMapper.shippingAddressToShippingAddressDTO(shippingAddress);
//        BillingAddressDTO billingAddressDTO = billingAddressMapper.billingAddressToBillingAddressDTO(billingAddress);

        List<OrderCreatedDTO> orderCreatedDTOs = orderPage.getContent().stream()
                    .map(order -> orderMapper.orderToOrderCreatedDTO(order))
                    .toList();

        return new PagedResponse<>(
                orderCreatedDTOs,
                orderPage.getNumber(),
                orderPage.getSize(), orderPage.getTotalElements(),
                orderPage.getTotalPages()
        );
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
