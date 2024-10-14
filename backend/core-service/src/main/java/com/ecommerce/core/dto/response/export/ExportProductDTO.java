package com.ecommerce.core.dto.response.export;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportProductDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3874028541549575018L;

    private UUID id;
    private String sku;
    private String name;
    private BigDecimal unitPrice;
    private int unitsInStock;
    private int unitsSold;
    private String imageUrl;
    private String description;
    private String categoryId;
    private String categoryName;
    //    private ProductCategoryDTO category;
    private Instant createdAt;
    private Instant updatedAt;
}
