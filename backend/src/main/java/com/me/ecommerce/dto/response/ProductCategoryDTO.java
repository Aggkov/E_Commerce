package com.me.ecommerce.dto.response;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
public class ProductCategoryDTO {

    private Long id;
    private String categoryName;
}
