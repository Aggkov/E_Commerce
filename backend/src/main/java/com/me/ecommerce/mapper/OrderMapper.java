package com.me.ecommerce.mapper;

import com.me.ecommerce.dto.request.OrderDTO;
import com.me.ecommerce.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

// Link to the CustomerMapper
@Mapper(componentModel = "spring"
//        , uses = CustomerMapper.class
)
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO orderToOrderDTO(Order order);

    @Mapping(target = "totalPrice", source = "orderDTO.orderInfoDTO.totalPrice")
    @Mapping(target = "totalQuantity", source = "orderDTO.orderInfoDTO.totalQuantity")
    @Mapping(target = "status", source = "orderDTO.orderInfoDTO.status")
//    @Mapping(target = "customer", source = "customerDTO") // Map the entire customer object using CustomerMapper
    Order orderDTOtoOrder(OrderDTO orderDTO);

}
