package com.me.ecommerce.controller;


import com.me.ecommerce.dto.response.ApiResponse;
import com.me.ecommerce.dto.response.ProductCategoryDTO;
import com.me.ecommerce.entity.ProductCategory;
import com.me.ecommerce.service.ProductCategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-category")
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
    public ResponseEntity<ProductCategory> getProductCategoryById(@PathVariable Long id) {
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
    public ResponseEntity<ProductCategory> updateProductCategory(@PathVariable Long id, @RequestBody ProductCategory productCategory) {
        return ResponseEntity.ok(productCategoryService.updateProductCategory(id, productCategory));
    }
}

