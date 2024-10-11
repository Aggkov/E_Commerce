package com.ecommerce.core.controller;


import com.ecommerce.core.dto.response.ProductCategoryDTO;
import com.ecommerce.core.entity.ProductCategory;
import com.ecommerce.core.service.ProductCategoryService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product-category")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @Autowired
    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> getProductCategories() {
        return ResponseEntity.ok(productCategoryService.getAllProductCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getProductCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(productCategoryService.getProductCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<ProductCategory> createProductCategory(@RequestBody ProductCategory productCategory) {
        return ResponseEntity.ok(productCategoryService.saveProductCategory(productCategory));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse> deleteProductCategory(@PathVariable Long id) {
//        return ResponseEntity.ok(productCategoryService.deleteProductCategory(id));
//    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategory> updateProductCategory(@PathVariable UUID id, @RequestBody ProductCategory productCategory) {
        return ResponseEntity.ok(productCategoryService.updateProductCategory(id, productCategory));
    }
}

