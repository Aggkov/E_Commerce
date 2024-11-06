package com.ecommerce.core.service.impl;

import com.ecommerce.core.dto.request.OrderDTO;
import com.ecommerce.core.entity.BillingAddress;
import com.ecommerce.core.entity.ShippingAddress;
import com.ecommerce.core.exception.ResourceNotFoundException;
import com.ecommerce.core.mapper.BillingAddressMapper;
import com.ecommerce.core.mapper.ShippingAddressMapper;
import com.ecommerce.core.repository.BillingAddressRepository;
import com.ecommerce.core.repository.ShippingAddressRepository;
import com.ecommerce.core.repository.StateRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl {

    private final ShippingAddressRepository shippingAddressRepository;
    private final BillingAddressRepository billingAddressRepository;
    private final ShippingAddressMapper shippingAddressMapper;
    private final BillingAddressMapper billingAddressMapper;
    private final StateRepository stateRepository;

    public ShippingAddress getShippingAddress(OrderDTO orderDTO) {
        // Check if the shipping address already exists in db
        Optional<ShippingAddress> existingShippingAddress = shippingAddressRepository.findShippingAddressByStreetAndZipCodeAndCityAndState(
                orderDTO.getUser().getShippingAddress().getStreet(),
                orderDTO.getUser().getShippingAddress().getZipCode(),
                orderDTO.getUser().getShippingAddress().getCity(),
                orderDTO.getUser().getShippingAddress().getState().getId());
        // Create or use existing shipping address
        ShippingAddress shippingAddress = existingShippingAddress.orElseGet(() -> {
            ShippingAddress newShippingAddress = shippingAddressMapper.shippingAddressDTOToAddress(orderDTO.getUser().getShippingAddress());
            newShippingAddress.setState(stateRepository.findById(
                            orderDTO.getUser().getShippingAddress().getState().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Shipping state not found", HttpStatus.NOT_FOUND)));
            return newShippingAddress;
        });
        return shippingAddress;
    }

    public BillingAddress getBillingAddress(OrderDTO orderDTO) {
        Optional<BillingAddress> existingBillingAddress = billingAddressRepository.findBillingAddressByIdOrStreetAndZipCodeAndCityAndState(
                orderDTO.getUser().getShippingAddress().getId(),
                orderDTO.getUser().getBillingAddress().getStreet(),
                orderDTO.getUser().getBillingAddress().getZipCode(),
                orderDTO.getUser().getBillingAddress().getCity(),
                orderDTO.getUser().getBillingAddress().getState().getId());

        BillingAddress billingAddress = existingBillingAddress.orElseGet(() -> {
            BillingAddress newBillingAddress = billingAddressMapper.billingAddressDTOToAddress(orderDTO.getUser().getBillingAddress());
            newBillingAddress.setState(stateRepository.findById(
                            orderDTO.getUser().getShippingAddress().getState().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Shipping state not found", HttpStatus.NOT_FOUND)));
            return newBillingAddress;
        });
        return billingAddress;
    }
}
