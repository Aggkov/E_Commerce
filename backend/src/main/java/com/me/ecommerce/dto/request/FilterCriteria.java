package com.me.ecommerce.dto.request;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterCriteria implements Serializable {
    @Serial
    private static final long serialVersionUID = -6608587024642534734L;
    String priceFrom;
    String priceTo;
    String selectedPriceRange;
    Map<String, Boolean> nameFilters = Map.of(
            "java", false,
            "javascript", false,
            "python", false,
            "vue", false,
            "csharp", false,
            "sql", false
            );

}
