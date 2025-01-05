//package com.ecommerce.core.kafka;
//
//import com.ecommerce.core.dto.request.OrderInfoDTO;
//import com.ecommerce.core.dto.request.OrderItemDTO;
//import com.ecommerce.core.dto.response.UserDTO;
//import jakarta.validation.constraints.NotBlank;
//import java.io.Serial;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.List;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class OrderSuccessEvent implements Serializable {
//    @Serial
//    private static final long serialVersionUID = 2412413464225363463L;
//    private String orderTrackingNumber;
//    private OrderInfoDTO orderInfo;
//    private List<OrderItemDTO> orderItems;
//    private UserDTO user;
//}
