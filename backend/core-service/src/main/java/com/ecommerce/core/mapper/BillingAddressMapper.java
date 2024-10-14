package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.request.BillingAddressDTO;
import com.ecommerce.core.entity.BillingAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = StateMapper.class)
public interface BillingAddressMapper {

    BillingAddress INSTANCE = Mappers.getMapper(BillingAddress.class);

    //    @Mapping(target = "id", source = "shippingAddressDTO.id")
//    @Mapping(target = "city", source = "shippingAddressDTO.city")
//    @Mapping(target = "street", source = "shippingAddressDTO.street")
//    @Mapping(target = "zipCode", source = "shippingAddressDTO.zipCode")
    @Mapping(target = "state", source = "state")
    BillingAddress billingAddressDTOToAddress(BillingAddressDTO billingAddressDTO);

    @Mapping(target = "state", source = "state")
    BillingAddressDTO billingAddressToBillingAddressDTO(BillingAddress billingAddress);
}
