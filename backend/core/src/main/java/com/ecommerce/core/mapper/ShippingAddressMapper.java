package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.request.ShippingAddressDTO;
import com.ecommerce.core.entity.ShippingAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = StateMapper.class)
public interface ShippingAddressMapper {

    ShippingAddress INSTANCE = Mappers.getMapper(ShippingAddress.class);

//    @Mapping(target = "id", source = "shippingAddressDTO.id")
//    @Mapping(target = "city", source = "shippingAddressDTO.city")
//    @Mapping(target = "street", source = "shippingAddressDTO.street")
//    @Mapping(target = "zipCode", source = "shippingAddressDTO.zipCode")
    @Mapping(target = "state", source = "state")
    ShippingAddress shippingAddressDTOToAddress(ShippingAddressDTO shippingAddress);

    @Mapping(target = "state", source = "state")
    ShippingAddress shippingAddressDTOToAddress(ShippingAddress shippingAddress);

}
