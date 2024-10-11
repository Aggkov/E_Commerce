package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.response.UserDTO;
import com.ecommerce.core.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        uses = {ShippingAddressMapper.class, BillingAddressMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

//    @Mapping(target = "firstName", source = "customerDTO.firstName")
//    @Mapping(target = "lastName", source = "customerDTO.lastName")
//    @Mapping(target = "email", source = "customerDTO.email")
    @Mapping(target = "shippingAddresses", ignore = true)
    @Mapping(target = "billingAddresses", ignore = true)
    User userDTOToUser(UserDTO userDTO);
}

