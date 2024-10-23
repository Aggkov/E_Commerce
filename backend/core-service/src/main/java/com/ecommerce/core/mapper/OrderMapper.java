package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.request.OrderDTO;
import com.ecommerce.core.dto.request.OrderItemDTO;
import com.ecommerce.core.dto.response.OrderCreatedDTO;
import com.ecommerce.core.dto.response.OrderSuccessDTO;
import com.ecommerce.core.dto.response.ProductDTO;
import com.ecommerce.core.entity.Order;
import com.ecommerce.core.entity.OrderItem;
import com.ecommerce.core.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

// Link to the CustomerMapper
@Mapper(componentModel = "spring", uses = { ShippingAddressMapper.class, BillingAddressMapper.class })
public interface OrderMapper {
    OrderDTO orderToOrderDTO(Order order);

    @Mapping(target = "totalPrice", source = "orderDTO.orderInfo.totalPrice")
    @Mapping(target = "totalQuantity", source = "orderDTO.orderInfo.totalQuantity")
    @Mapping(target = "status", source = "orderDTO.orderInfo.status")
    @Mapping(target = "orderItems", source = "orderItems")
    Order orderDTOtoOrder(OrderDTO orderDTO);

    // Mapping method for OrderItemDTO to OrderItem
    // TODO check other product fields if they are mapped ignore them
    @Mapping(target = "product.imageUrl", ignore = true)  // Ignore imageUrl in product mapping
    OrderItem orderItemDTOtoOrderItem(OrderItemDTO orderItemDTO);

//    @Mapping(target = "totalPrice", source = "orderDTO.orderInfo.totalPrice")
//    @Mapping(target = "totalQuantity", source = "orderDTO.orderInfo.totalQuantity")
    @Mapping(target = "status", source = "orderDTO.orderInfo.status")
    OrderSuccessDTO orderDTOToOrderSuccessDTO(OrderDTO orderDTO);

    @Mapping(target = "orderInfo.totalPrice", source = "order.totalPrice")
    @Mapping(target = "orderInfo.totalQuantity", source = "order.totalQuantity")
    @Mapping(target = "orderInfo.status", source = "order.status")
    @Mapping(target = "orderItems", source = "order.orderItems")
    @Mapping(target = "shippingAddress", ignore = true)
    @Mapping(target = "billingAddress", ignore = true)
    OrderCreatedDTO orderToOrderCreatedDTO(Order order);

    // Mapping for each OrderItem to OrderItemDTO
    @Mapping(target = "product", source = "product")
    @Mapping(target = "quantity", source = "quantity")
    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);

    // Mapping only specific fields of Product to ProductDTO for this use case
    @Mapping(target = "name", source = "name")
    @Mapping(target = "imageUrl", ignore = true  /*source = "imageUrl" fix later */)
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "sku", ignore = true)  // Ignore any other fields from ProductDTO
    @Mapping(target = "unitsInStock", ignore = true)  // Ignore any other fields from ProductDTO
    @Mapping(target = "description", ignore = true)  // Ignore any other fields from ProductDTO
    @Mapping(target = "createdAt", ignore = true)  // Ignore any other fields from ProductDTO
    @Mapping(target = "updatedAt", ignore = true)  // Ignore any other fields from ProductDTO
    ProductDTO mapLimitedProductFields(Product product);
}
