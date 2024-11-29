package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.request.OrderDTO;
import com.ecommerce.core.dto.request.OrderItemDTO;
import com.ecommerce.core.dto.response.OrderCreatedDTO;
import com.ecommerce.core.dto.response.OrderSuccessDTO;
import com.ecommerce.core.dto.response.ProductDTO;
import com.ecommerce.core.entity.Order;
import com.ecommerce.core.entity.OrderItem;
import com.ecommerce.core.entity.Product;
import com.ecommerce.core.exception.BadRequestException;
import com.ecommerce.core.kafka.OrderSuccessEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;

// Link to the CustomerMapper
@Mapper(componentModel = "spring", uses = { ShippingAddressMapper.class, BillingAddressMapper.class })
public interface OrderMapper {
    OrderDTO orderToOrderDTO(Order order);

    @Mapping(target = "totalPrice", source = "orderDTO.orderInfo.totalPrice")
    @Mapping(target = "totalQuantity", source = "orderDTO.orderInfo.totalQuantity")
    @Mapping(target = "status", source = "orderDTO.orderInfo.status")
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "user", ignore = true)
    Order orderDTOtoOrder(OrderDTO orderDTO);

//    @Mapping(target = "orderTrackingNumber", source = "order.orderTrackingNumber")
    @Mapping(target = "orderInfo.totalPrice", source = "order.totalPrice")
    @Mapping(target = "orderInfo.totalQuantity", source = "order.totalQuantity")
    @Mapping(target = "orderInfo.status", source = "order.status")
    @Mapping(target = "orderItems", source = "order.orderItems")
    @Mapping(target = "user.email", source = "order.user.email")
    @Mapping(target = "user.shippingAddress", source = "shippingAddress")
    @Mapping(target = "user.billingAddress", source = "billingAddress")
    OrderSuccessDTO orderToOrderSuccessDTO(Order order);

    @Mapping(target = "orderInfo.totalPrice", source = "order.totalPrice")
    @Mapping(target = "orderInfo.totalQuantity", source = "order.totalQuantity")
    @Mapping(target = "orderInfo.status", source = "order.status")
    @Mapping(target = "orderItems", source = "order.orderItems")
    @Mapping(target = "shippingAddress", source = "shippingAddress")
    @Mapping(target = "billingAddress", source = "billingAddress")
    OrderCreatedDTO orderToOrderCreatedDTO(Order order);

    OrderSuccessEvent orderSuccessDTOtoOrderSuccessEvent(OrderSuccessDTO orderSuccessDTO);

    // Mapping for each OrderItem to OrderItemDTO
    @Mapping(target = "product", source = "product")
    @Mapping(target = "quantity", source = "quantity")
    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);

    // Mapping only specific fields of Product to ProductDTO for this use case
    @Mapping(target = "name", source = "name")
    @Mapping(target = "imageUrl", source = "product", qualifiedByName = "imageUrlToBytes")
    @Mapping(target = "unitPrice", source = "unitPrice")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "unitsInStock", ignore = true)
    @Mapping(target = "unitsSold", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProductDTO mapLimitedProductFields(Product product);

    @Named("imageUrlToBytes")
    default byte[] imageUrlToBytes(Product product) {
        if (product == null || (product.getImageUrl().isEmpty())) {
            return null;
        }
        try {
            String currentWorkingDir = System.getProperty("user.dir");
            Path filePath;
            // If the working directory ends with "core-service", move up to the root directory
            if (!currentWorkingDir.endsWith("core-service")) {
                // Move down if in root
                filePath = Paths.get(currentWorkingDir, "backend", "core-service", product.getImageUrl());
            } else {
                filePath = Paths.get(System.getProperty("user.dir"), product.getImageUrl());
            }
            if (!Files.exists(filePath)) {
                throw new BadRequestException("File does not exist in this path", HttpStatus.BAD_REQUEST);
            }
            return Files.readAllBytes(filePath);
        }
        catch (IOException e) {
            System.out.println("IO error occured: " + product.getImageUrl());
            return null;
        }
    }
}
