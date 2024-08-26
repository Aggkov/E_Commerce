package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.ProductCategoryDTO;
import com.me.ecommerce.entity.ProductCategory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryService {
    List<ProductCategoryDTO> getAllProductCategories();
    ProductCategory getProductCategoryById(UUID id);
    ProductCategory saveProductCategory(ProductCategory productCategory);
    void deleteProductCategory(UUID id);
    ProductCategory updateProductCategory(UUID id, ProductCategory productCategory);
}

