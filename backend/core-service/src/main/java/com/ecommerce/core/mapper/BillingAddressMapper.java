package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.request.BillingAddressDTO;
import com.ecommerce.core.entity.BillingAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = StateMapper.class)
public interface BillingAddressMapper {

    @Mapping(target = "state", source = "state")
    BillingAddress billingAddressDTOToAddress(BillingAddressDTO billingAddressDTO);

    @Mapping(target = "state", source = "state")
    @Mapping(target = "id", ignore = true)
    BillingAddressDTO billingAddressToBillingAddressDTO(BillingAddress billingAddress);
}
