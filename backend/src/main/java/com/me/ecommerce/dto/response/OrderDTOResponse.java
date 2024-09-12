package com.me.ecommerce.dto.response;

import com.me.ecommerce.dto.request.OrderDTO;
import com.me.ecommerce.dto.request.OrderItemDTO;
import com.me.ecommerce.dto.request.ShippingAddressDTO;
import com.me.ecommerce.entity.Order;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTOResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1959889964645223660L;

    private String orderTrackingNumber;

//    private Integer quantity;
//
//    private String name;
//
//    private BigDecimal unitPrice;
//
//    private String imageUrl;

    private List<OrderItemDTO> orderItems;

    private CustomerDTO customer;



    public OrderDTOResponse(String orderTrackingNumber,
                            OrderDTO orderDTO) {
        this.orderTrackingNumber = orderTrackingNumber;
//        orderDTO.getOrderItemList().forEach(orderItemDTO -> {
//            imageUrls.add(orderItemDTO.getProduct().getImageUrl());
//        });
        this.orderItems = orderDTO.getOrderItemList();
//        imageUrls.forEach(image -> {
//            this.orderItems.forEach(orderItemDTO -> {
//                ProductDTO productDTO = orderItemDTO.getProduct();
//                productDTO.setImageUrl(image);
//            });
//        });
        this.customer = orderDTO.getCustomer();
    }
}
