package com.me.ecommerce.mapper;

import com.me.ecommerce.dto.request.OrderDTO;
import com.me.ecommerce.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO orderToOrderDTO(Order order);

    Order orderDTOtoOrder(OrderDTO orderDTO);

}
