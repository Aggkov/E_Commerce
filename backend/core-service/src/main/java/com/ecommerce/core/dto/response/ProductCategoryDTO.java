package com.ecommerce.core.dto.response;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCategoryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5497628019981730018L;
    private UUID id;
    private String categoryName;
}
