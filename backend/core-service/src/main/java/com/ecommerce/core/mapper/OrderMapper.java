package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.request.OrderDTO;
import com.ecommerce.core.entity.Order;
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

    @Mapping(target = "totalPrice", source = "orderDTO.orderInfo.totalPrice")
    @Mapping(target = "totalQuantity", source = "orderDTO.orderInfo.totalQuantity")
    @Mapping(target = "status", source = "orderDTO.orderInfo.status")
//    @Mapping(target = "customer", source = "customerDTO") // Map the entire customer object using CustomerMapper
    Order orderDTOtoOrder(OrderDTO orderDTO);

}
