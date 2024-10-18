package com.ecommerce.core.service.impl;

import com.ecommerce.core.dto.request.OrderDTO;
import com.ecommerce.core.dto.response.OrderDTOResponse;
import com.ecommerce.core.entity.BillingAddress;
import com.ecommerce.core.entity.Order;
import com.ecommerce.core.entity.OrderItem;
import com.ecommerce.core.entity.Product;
import com.ecommerce.core.entity.ShippingAddress;
import com.ecommerce.core.entity.User;
import com.ecommerce.core.exception.ResourceNotFoundException;
import com.ecommerce.core.mapper.OrderItemMapper;
import com.ecommerce.core.mapper.OrderMapper;
import com.ecommerce.core.mapper.UserMapper;
import com.ecommerce.core.repository.UserRepository;
import com.ecommerce.core.repository.OrderRepository;
import com.ecommerce.core.repository.ProductRepository;
import com.ecommerce.core.service.OrderService;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final AddressServiceImpl addressService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public OrderDTOResponse createNewOrder(OrderDTO orderDTO) {
        // 1. Map OrderDTO to Order entity
        Order order = orderMapper.orderDTOtoOrder(orderDTO);

        // 2. Generate a unique order tracking number
        String orderTrackingNumber;
        do {
            orderTrackingNumber = generateOrderTrackingNumber();
        } while (orderRepository.findOrderTrackingNumber(orderTrackingNumber) != null);
        order.setOrderTrackingNumber(orderTrackingNumber);

//        User user = userRepository.findById(orderDTO.getUser().ge)
        User user = userMapper.userDTOToUser(orderDTO.getUser());

        // 4. Get or create shipping and billing addresses
        ShippingAddress shippingAddress = addressService.getShippingAddress(orderDTO);
        BillingAddress billingAddress = addressService.getBillingAddress(orderDTO);

        // TODO combine all in user entity
        user.getShippingAddresses().add(shippingAddress);
        user.getBillingAddresses().add(billingAddress);
        shippingAddress.getUsers().add(user);
        billingAddress.getUsers().add(user);

        // 5. Batch fetch products and map them to their IDs, find products requested by client dto
        Set<UUID> productIds = orderDTO.getOrderItemList().stream().map(orderItemDTO -> orderItemDTO.getProduct().getId()).collect(Collectors.toSet());

        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream().collect(Collectors.toMap(Product::getId, product -> product));
        // prepare orderItem
        orderDTO.getOrderItemList().forEach(orderItemDTO -> {
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
        userRepository.save(user);

        return new OrderDTOResponse(orderTrackingNumber, orderDTO);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
