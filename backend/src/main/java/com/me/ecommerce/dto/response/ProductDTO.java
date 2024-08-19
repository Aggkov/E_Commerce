package com.me.ecommerce.dto.response;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private long id;
    private String name;
    private BigDecimal unitPrice;
    private int unitsInStock;
    private String imageUrl;
    private String description;

}
