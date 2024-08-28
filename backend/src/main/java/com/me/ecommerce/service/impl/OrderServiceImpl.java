package com.me.ecommerce.service.impl;

import com.me.ecommerce.dto.request.BillingAddressDTO;
import com.me.ecommerce.dto.request.OrderDTO;
import com.me.ecommerce.dto.request.ShippingAddressDTO;
import com.me.ecommerce.dto.response.OrderDTOResponse;
import com.me.ecommerce.entity.Address;
import com.me.ecommerce.entity.Customer;
import com.me.ecommerce.entity.Order;
import com.me.ecommerce.entity.OrderItem;
import com.me.ecommerce.entity.Product;
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

        Address shippingAddress = getShippingAddress(orderDTO);

        Address billingAddress = getBillingAddress(orderDTO, shippingAddress);

        // Step 4: Create customer and link addresses
        Customer customer = customerMapper.customerDTOToCustomer(orderDTO.getCustomerDTO());
        customer.setShippingAddress(shippingAddress);
        customer.setBillingAddress(billingAddress);

        // 5. Batch fetch products and map them to their IDs, find products requested by client dto
        Set<UUID> productIds = orderDTO.getOrderItemDTOList().stream()
                .map(orderItemDTO -> orderItemDTO.getProductDTO().getId())
                .collect(Collectors.toSet());

        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        // prepare orderItem
        orderDTO.getOrderItemDTOList()
                .forEach(orderItemDTO -> {
                    Product product = productMap.get(orderItemDTO.getProductDTO().getId());
                    if (product == null) {
                        throw new RuntimeException("Product not found: " + orderItemDTO.getProductDTO().getId());
                    }
                    OrderItem orderItem = orderItemMapper.orderItemDTOToOrderItem(orderItemDTO);
                    // parent add child
                    product.addOrderItem(orderItem);
                    // parent add child
                    order.add(orderItem);
                });
        // parent add child
        customer.add(order);
        // cascade persist
        customerRepository.save(customer);

        return new OrderDTOResponse(orderTrackingNumber);
    }

    private Address getShippingAddress(OrderDTO orderDTO) {
        // Step 1: Check if the shipping address already exists in db
        Optional<Address> existingShippingAddress = addressRepository.findByIdOrStreetAndZipCodeAndCityAndState(
                orderDTO.getCustomerDTO().getShippingAddressDTO().getId(),
                orderDTO.getCustomerDTO().getShippingAddressDTO().getStreet(),
                orderDTO.getCustomerDTO().getShippingAddressDTO().getZipCode(),
                orderDTO.getCustomerDTO().getShippingAddressDTO().getCity(),
                orderDTO.getCustomerDTO().getShippingAddressDTO().getStateDTO().getId()
        );

        // Step 2: Create or use existing shipping address
        Address shippingAddress = existingShippingAddress.orElseGet(() -> {
            Address newAddress = addressMapper.shippingAddressDTOToAddress(orderDTO.getCustomerDTO().getShippingAddressDTO());
            newAddress.setState(stateRepository.findById(orderDTO.getCustomerDTO().getShippingAddressDTO().getStateDTO().getId())
                    .orElseThrow(() -> new RuntimeException("Shipping state not found")));
            return addressRepository.save(newAddress); // Save the new address
        });
        return shippingAddress;
    }

    private Address getBillingAddress(OrderDTO orderDTO, Address shippingAddress) {
        // Step 3: Determine if billing address is the same as shipping address
        Address billingAddress;

        boolean shippingDTOEqualsBillingDTO = isShippingDTOEqualsBillingDTO(orderDTO);

        if (shippingDTOEqualsBillingDTO) {
            billingAddress = shippingAddress; // Reuse the shipping address
        } else {
            Optional<Address> existingBillingAddress = addressRepository.findByIdOrStreetAndZipCodeAndCityAndState(
                    orderDTO.getCustomerDTO().getShippingAddressDTO().getId(),
                    orderDTO.getCustomerDTO().getBillingAddressDTO().getStreet(),
                    orderDTO.getCustomerDTO().getBillingAddressDTO().getZipCode(),
                    orderDTO.getCustomerDTO().getBillingAddressDTO().getCity(),
                    orderDTO.getCustomerDTO().getBillingAddressDTO().getStateDTO().getId()
            );

            billingAddress = existingBillingAddress.orElseGet(() -> {
                Address newAddress = addressMapper.billingAddressDTOToAddress(orderDTO.getCustomerDTO().getBillingAddressDTO());
                newAddress.setState(stateRepository.findById(orderDTO.getCustomerDTO().getBillingAddressDTO().getStateDTO().getId())
                        .orElseThrow(() -> new RuntimeException("Billing state not found")));
                return addressRepository.save(newAddress); // Save the new address
            });
        }
        return billingAddress;
    }

    private static boolean isShippingDTOEqualsBillingDTO(OrderDTO orderDTO) {
        ShippingAddressDTO shippingAddressDTO = orderDTO.getCustomerDTO().getShippingAddressDTO();
        BillingAddressDTO billingAddressDTO = orderDTO.getCustomerDTO().getBillingAddressDTO();

            return shippingAddressDTO.getStreet().equals(billingAddressDTO.getStreet()) &&
                shippingAddressDTO.getCity().equals(billingAddressDTO.getCity()) &&
                shippingAddressDTO.getZipCode().equals(billingAddressDTO.getZipCode()) &&
                shippingAddressDTO.getStateDTO().getId().equals(billingAddressDTO.getStateDTO().getId());
    }


    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
