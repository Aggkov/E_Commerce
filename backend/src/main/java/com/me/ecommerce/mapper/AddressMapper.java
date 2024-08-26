package com.me.ecommerce.mapper;

import com.me.ecommerce.dto.request.BillingAddressDTO;
import com.me.ecommerce.dto.request.ShippingAddressDTO;
import com.me.ecommerce.dto.response.CustomerDTO;
import com.me.ecommerce.entity.Address;
import com.me.ecommerce.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = StateMapper.class)
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

//    @Mapping(target = "id", source = "shippingAddressDTO.id")
//    @Mapping(target = "city", source = "shippingAddressDTO.city")
//    @Mapping(target = "street", source = "shippingAddressDTO.street")
//    @Mapping(target = "zipCode", source = "shippingAddressDTO.zipCode")
    @Mapping(target = "state", source = "stateDTO")
    Address shippingAddressDTOToAddress(ShippingAddressDTO shippingAddressDTO);

    @Mapping(target = "state", source = "stateDTO")
    Address billingAddressDTOToAddress(BillingAddressDTO billingAddressDTO);

}
