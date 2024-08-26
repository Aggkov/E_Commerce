package com.me.ecommerce.mapper;

import com.me.ecommerce.dto.request.OrderItemDTO;
import com.me.ecommerce.dto.response.CustomerDTO;
import com.me.ecommerce.entity.Customer;
import com.me.ecommerce.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

//    @Mapping(target = "product.id", source = "orderItemDTO.productDTO.id")
    OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO);
}
