package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.ProductCategoryDTO;
import com.me.ecommerce.entity.ProductCategory;
import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {
    List<ProductCategoryDTO> getAllProductCategories();
    ProductCategory getProductCategoryById(Long id);
    ProductCategory saveProductCategory(ProductCategory productCategory);
    void deleteProductCategory(Long id);
    ProductCategory updateProductCategory(Long id, ProductCategory productCategory);
}

