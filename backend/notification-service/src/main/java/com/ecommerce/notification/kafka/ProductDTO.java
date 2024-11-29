package com.ecommerce.notification.kafka;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5107636052390868741L;
    private UUID id;
    private String sku;
    private String name;
    private BigDecimal unitPrice;
    private int unitsInStock;
    private int unitsSold;
    private byte[] imageUrl;
    private String description;
    private String categoryId;
    private String categoryName;
    private Instant createdAt;
    private Instant updatedAt;

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", unitsInStock=" + unitsInStock +
                ", unitsSold=" + unitsSold +
                ", imageUrl=" + Arrays.toString(imageUrl) +
                ", description='" + description + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
