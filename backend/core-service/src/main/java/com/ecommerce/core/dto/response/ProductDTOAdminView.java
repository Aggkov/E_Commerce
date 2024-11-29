package com.ecommerce.core.dto.response;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTOAdminView {
    private String sku;
    private String name;
    private BigDecimal unitPrice;
    private int unitsInStock;
    private int unitsSold;
    private boolean active;
}
