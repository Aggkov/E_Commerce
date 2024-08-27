package com.me.ecommerce.service.impl;

import com.me.ecommerce.dto.request.OrderDTO;
import com.me.ecommerce.dto.request.OrderItemDTO;
import com.me.ecommerce.dto.response.OrderDTOResponse;
import com.me.ecommerce.entity.Address;
import com.me.ecommerce.entity.Customer;
import com.me.ecommerce.entity.Order;
import com.me.ecommerce.entity.OrderItem;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.entity.State;
import com.me.ecommerce.mapper.AddressMapper;
import com.me.ecommerce.mapper.CustomerMapper;
import com.me.ecommerce.mapper.OrderItemMapper;
import com.me.ecommerce.mapper.OrderMapper;
import com.me.ecommerce.mapper.StateMapper;
import com.me.ecommerce.repository.AddressRepository;
import com.me.ecommerce.repository.CustomerRepository;
import com.me.ecommerce.repository.OrderRepository;
import com.me.ecommerce.repository.ProductRepository;
import com.me.ecommerce.repository.StateRepository;
import com.me.ecommerce.service.OrderService;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;
    private final StateMapper stateMapper;
    private final StateRepository stateRepository;

    @Autowired
    public OrderServiceImpl(CustomerRepository customerRepository, OrderRepository orderRepository, ProductRepository productRepository, AddressRepository addressRepository, OrderMapper orderMapper, OrderItemMapper orderItemMapper, CustomerMapper customerMapper, AddressMapper addressMapper, StateMapper stateMapper, StateRepository stateRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.customerMapper = customerMapper;
        this.addressMapper = addressMapper;
        this.stateMapper = stateMapper;
        this.stateRepository = stateRepository;
    }

    @Override
    @Transactional
    public OrderDTOResponse createOrder(OrderDTO orderDTO) {
        // 1. Map OrderDTO to Order entity
        Order order = orderMapper.orderDTOtoOrder(orderDTO);

        // 2. Generate a unique order tracking number
        String orderTrackingNumber;
        do {
            orderTrackingNumber = generateOrderTrackingNumber();
        } while (orderRepository.findOrderTrackingNumber(orderTrackingNumber) != null);
        order.setOrderTrackingNumber(orderTrackingNumber);

        Address shippingAddress = addressMapper.shippingAddressDTOToAddress(orderDTO.getCustomerDTO().getShippingAddressDTO());
        Address billingAddress = addressMapper.billingAddressDTOToAddress(orderDTO.getCustomerDTO().getBillingAddressDTO());

        // Attach states to the addresses
        shippingAddress.setState(stateRepository.findById(orderDTO.getCustomerDTO().getShippingAddressDTO().getStateDTO().getId())
                .orElseThrow(() -> new RuntimeException("Shipping state not found")));
        billingAddress.setState(stateRepository.findById(orderDTO.getCustomerDTO().getBillingAddressDTO().getStateDTO().getId())
                .orElseThrow(() -> new RuntimeException("Billing state not found")));

        // 4. Map CustomerDTO to Customer entity
        Customer customer = customerMapper.customerDTOToCustomer(orderDTO.getCustomerDTO());
        customer.setAddress(shippingAddress);
        customer.setAddress(billingAddress);

        // 5. Batch fetch products and map them to their IDs
        Set<UUID> productIds = orderDTO.getOrderItemDTOList().stream()
                .map(orderItemDTO -> orderItemDTO.getProductDTO().getId())
                .collect(Collectors.toSet());

        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        // Now iterate through the OrderItemDTOs and create OrderItems
        orderDTO.getOrderItemDTOList()
                .forEach(orderItemDTO -> {
                    // Retrieve the product from the map
                    Product product = productMap.get(orderItemDTO.getProductDTO().getId());
                    if (product == null) {
                        throw new RuntimeException("Product not found: " + orderItemDTO.getProductDTO().getId());
                    }
                    // Map DTO to OrderItem
                    OrderItem orderItem = orderItemMapper.orderItemDTOToOrderItem(orderItemDTO);
                    product.addOrderItem(orderItem); // Set the product in each order item
                    order.add(orderItem);
                });
        customer.add(order);
//        orderRepository.save(order);
        customerRepository.save(customer);
//                .collect(Collectors.toSet());
        // 6. Add the order to the customer

        // 7. Persist the customer (cascading should handle order and other entities)
//        customerRepository.save(customer);

        return new OrderDTOResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
