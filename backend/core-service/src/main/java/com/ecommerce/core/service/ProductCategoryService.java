package com.ecommerce.core.service;

import com.ecommerce.core.dto.response.ProductCategoryDTO;
import com.ecommerce.core.entity.ProductCategory;
import java.util.List;
import java.util.UUID;

public interface ProductCategoryService {
    List<ProductCategoryDTO> getAllProductCategories();
    ProductCategory getProductCategoryById(UUID id);
    ProductCategory saveProductCategory(ProductCategory productCategory);
    void deleteProductCategory(UUID id);
    ProductCategory updateProductCategory(UUID id, ProductCategory productCategory);
}

