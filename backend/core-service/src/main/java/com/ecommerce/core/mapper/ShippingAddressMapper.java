package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.request.ShippingAddressDTO;
import com.ecommerce.core.entity.ShippingAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = StateMapper.class)
public interface ShippingAddressMapper {

    @Mapping(target = "state", source = "state")
    ShippingAddress shippingAddressDTOToAddress(ShippingAddressDTO shippingAddress);

    @Mapping(target = "state", source = "state")
    @Mapping(target = "id", ignore = true)
    ShippingAddressDTO shippingAddressToShippingAddressDTO(ShippingAddress shippingAddress);

}
