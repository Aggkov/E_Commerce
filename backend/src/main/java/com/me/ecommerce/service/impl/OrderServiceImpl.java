package com.me.ecommerce.service.impl;

import com.me.ecommerce.dto.request.BillingAddressDTO;
import com.me.ecommerce.dto.request.OrderDTO;
import com.me.ecommerce.dto.request.ShippingAddressDTO;
import com.me.ecommerce.dto.response.OrderDTOResponse;
import com.me.ecommerce.entity.BillingAddress;
import com.me.ecommerce.entity.Customer;
import com.me.ecommerce.entity.Order;
import com.me.ecommerce.entity.OrderItem;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.entity.ShippingAddress;
import com.me.ecommerce.mapper.BillingAddressMapper;
import com.me.ecommerce.mapper.CustomerMapper;
import com.me.ecommerce.mapper.OrderItemMapper;
import com.me.ecommerce.mapper.OrderMapper;
import com.me.ecommerce.mapper.ShippingAddressMapper;
import com.me.ecommerce.mapper.StateMapper;
import com.me.ecommerce.repository.BillingAddressRepository;
import com.me.ecommerce.repository.CustomerRepository;
import com.me.ecommerce.repository.OrderRepository;
import com.me.ecommerce.repository.ProductRepository;
import com.me.ecommerce.repository.ShippingAddressRepository;
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
    private final ShippingAddressRepository shippingAddressRepository;
    private final BillingAddressRepository billingAddressRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CustomerMapper customerMapper;
    private final ShippingAddressMapper shippingAddressMapper;
    private final BillingAddressMapper billingAddressMapper;
    private final StateMapper stateMapper;
    private final StateRepository stateRepository;

    @Autowired
    public OrderServiceImpl(CustomerRepository customerRepository,
                            OrderRepository orderRepository,
                            ProductRepository productRepository,
                            ShippingAddressRepository shippingAddressRepository,
                            BillingAddressRepository billingAddressRepository,
                            OrderMapper orderMapper,
                            OrderItemMapper orderItemMapper,
                            CustomerMapper customerMapper,
                            ShippingAddressMapper shippingAddressMapper,
                            BillingAddressMapper billingAddressMapper,
                            StateMapper stateMapper,
                            StateRepository stateRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.shippingAddressRepository = shippingAddressRepository;
        this.billingAddressRepository = billingAddressRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.customerMapper = customerMapper;
        this.shippingAddressMapper = shippingAddressMapper;
        this.billingAddressMapper = billingAddressMapper;
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

        Customer customer = customerMapper.customerDTOToCustomer(orderDTO.getCustomer());

        /*
        address obj has shipping and billing info
        so 1 address obj must ref a customer if same shipp and bill

        if shipp and bill diff need 2 addresses that ref the same customer
         */

        // 4. Get or create shipping and billing addresses
        ShippingAddress shippingAddress = getShippingAddress(orderDTO);
        BillingAddress billingAddress = getBillingAddress(orderDTO);

        customer.getShippingAddresses().add(shippingAddress);
        customer.getBillingAddresses().add(billingAddress);
        shippingAddress.getCustomers().add(customer);
        billingAddress.getCustomers().add(customer);

//        if(!shippingAddress.equals(billingAddress)) {
//           addressRepository.save(shippingAddress);
//           addressRepository.save(billingAddress);
//        } else {
//            addressRepository.save(billingAddress);
//        }

        // 5. Batch fetch products and map them to their IDs, find products requested by client dto
        Set<UUID> productIds = orderDTO.getOrderItemList().stream()
                .map(orderItemDTO -> orderItemDTO.getProduct().getId())
                .collect(Collectors.toSet());

        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        // prepare orderItem
        orderDTO.getOrderItemList()
                .forEach(orderItemDTO -> {
                    Product product = productMap.get(orderItemDTO.getProduct().getId());
                    if (product == null) {
                        throw new RuntimeException("Product not found: " + orderItemDTO.getProduct().getId());
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

    private ShippingAddress getShippingAddress(OrderDTO orderDTO) {
        // Step 1: Check if the shipping address already exists in db
        Optional<ShippingAddress> existingShippingAddress = shippingAddressRepository.findShippingAddressByIdOrStreetAndZipCodeAndCityAndState(
                orderDTO.getCustomer().getShippingAddress().getId(),
                orderDTO.getCustomer().getShippingAddress().getStreet(),
                orderDTO.getCustomer().getShippingAddress().getZipCode(),
                orderDTO.getCustomer().getShippingAddress().getCity(),
                orderDTO.getCustomer().getShippingAddress().getState().getId()
        );
//         need only one address return existign or new, that address has references to cust ship and bill.
        // Step 2: Create or use existing shipping address
        ShippingAddress shippingAddress = existingShippingAddress.orElseGet(() -> {
            ShippingAddress newShippingAddress = shippingAddressMapper.shippingAddressDTOToAddress(orderDTO.getCustomer().getShippingAddress());
            newShippingAddress.setState(stateRepository.findById(orderDTO.getCustomer().getShippingAddress().getState().getId())
                    .orElseThrow(() -> new RuntimeException("Shipping state not found")));
//            return addressRepository.save(newAddress); // Save the new address
            return newShippingAddress;
        });
        return shippingAddress;
    }

    private BillingAddress getBillingAddress(OrderDTO orderDTO) {
        Optional<BillingAddress> existingBillingAddress = billingAddressRepository.findBillingAddressByIdOrStreetAndZipCodeAndCityAndState(
                orderDTO.getCustomer().getShippingAddress().getId(),
                orderDTO.getCustomer().getBillingAddress().getStreet(),
                orderDTO.getCustomer().getBillingAddress().getZipCode(),
                orderDTO.getCustomer().getBillingAddress().getCity(),
                orderDTO.getCustomer().getBillingAddress().getState().getId()
        );

        BillingAddress billingAddress = existingBillingAddress.orElseGet(() -> {
            BillingAddress newBillingAddress = billingAddressMapper.billingAddressDTOToAddress(orderDTO.getCustomer().getBillingAddress());
            newBillingAddress.setState(stateRepository.findById(orderDTO.getCustomer().getShippingAddress().getState().getId())
                    .orElseThrow(() -> new RuntimeException("Shipping state not found")));
//            return addressRepository.save(newAddress); // Save the new address
            return newBillingAddress;
        });
        return billingAddress;
    }

//    private static boolean isShippingDTOEqualsBillingDTO(OrderDTO orderDTO) {
//        ShippingAddressDTO shippingAddressDTO = orderDTO.getCustomer().getShippingAddress();
//        BillingAddressDTO billingAddressDTO = orderDTO.getCustomer().getBillingAddress();
//
//        return shippingAddressDTO.getStreet().equals(billingAddressDTO.getStreet()) &&
//                shippingAddressDTO.getCity().equals(billingAddressDTO.getCity()) &&
//                shippingAddressDTO.getZipCode().equals(billingAddressDTO.getZipCode()) &&
//                shippingAddressDTO.getState().getId().equals(billingAddressDTO.getState().getId());
//    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
