package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.request.OrderItemDTO;
import com.ecommerce.core.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

//    @Mapping(target = "product.id", source = "orderItemDTO.productDTO.id")
    @Mapping(target = "product.imageUrl", ignore = true)
    @Mapping(target = "product.active", ignore = true)
    @Mapping(target = "product.category", ignore = true)
    @Mapping(target = "product.orderItems", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO);
}
