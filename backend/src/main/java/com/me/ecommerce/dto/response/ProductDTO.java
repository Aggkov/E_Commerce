package com.me.ecommerce.dto.response;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7559629798078881826L;
    private long id;
    private String name;
    private BigDecimal unitPrice;
    private int unitsInStock;
    private String imageUrl;
    private String description;
}
