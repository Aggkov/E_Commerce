package com.me.ecommerce.dto.request;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterCriteria implements Serializable {
    @Serial
    private static final long serialVersionUID = -6608587024642534734L;
    private UUID categoryId;
    String minPrice;
    String maxPrice;
    String priceRange;
    private List<String> nameFilters;
    private int page;
    private int size;

}
