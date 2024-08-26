package com.me.ecommerce.service.impl;

import com.me.ecommerce.dto.response.ProductCategoryDTO;
import com.me.ecommerce.entity.ProductCategory;
import com.me.ecommerce.mapper.ProductCategoryMapper;
import com.me.ecommerce.repository.ProductCategoryRepository;
import com.me.ecommerce.service.ProductCategoryService;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    @Autowired
    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository,
                                      ProductCategoryMapper productCategoryMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public List<ProductCategoryDTO> getAllProductCategories() {
       List<ProductCategory> productCategories = productCategoryRepository.findAll();

        List<ProductCategoryDTO> productCategoryDTOS = productCategories.stream()
               .sorted(Comparator.comparing(ProductCategory::getCategoryName))
               .map(productCategoryMapper::productCategoryToProductCategoryDTO)
               .toList();
//
        return productCategoryDTOS;
    }

    @Override
    public ProductCategory getProductCategoryById(UUID id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Category not found"));
    }

    @Override
    public ProductCategory saveProductCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public void deleteProductCategory(UUID id) {
        productCategoryRepository.deleteById(id);
    }

    @Override
    public ProductCategory updateProductCategory(UUID id, ProductCategory productCategory) {
        // Find the existing product category by ID
        ProductCategory existingCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Category not found"));

        // Update the existing category with new values
        existingCategory.setCategoryName(productCategory.getCategoryName());
        // Update other fields as necessary

        // Save and return the updated category
        return productCategoryRepository.save(existingCategory);
    }
}

