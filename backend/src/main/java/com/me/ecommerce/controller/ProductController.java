package com.me.ecommerce.controller;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.service.ProductService;
import com.me.ecommerce.utils.AppConstants;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200") // Replace with your frontend URL
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public PagedResponse<ProductDTO> getAllProducts(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        return productService.getAllProducts(page,size);
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @GetMapping("/category/{id}")
//    @GetMapping("/category")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategoryPaginated(
//            @RequestParam(name = "id", required = true) Long id,
            @PathVariable Long id,
            Pageable pageable) {
        Page<ProductDTO> productDTOs = productService.getProductsByCategory(id, pageable);

        if (productDTOs.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content if no products found
        }
        return ResponseEntity.ok(productDTOs); // 200 OK with the list of products
    }

    @GetMapping("/search/paginated")
    public ResponseEntity<Page<ProductDTO>> searchProductByKeywordsPaginated(
            @RequestParam(name = "keywords") String keywords,
//            @RequestParam(name = "limit", required = false) Integer limit,
            Pageable pageable) {
        Page<ProductDTO> productDTOs = productService.searchProductByKeywordsPaginated(keywords,
                pageable);
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProductByKeywords(
            @RequestParam(name = "keywords") String keywords,
//            @RequestParam(name = "limit") Integer limit,
            Pageable pageable
    ) {
        List<ProductDTO> productDTOs = productService.searchProductByKeywords(keywords);
        return ResponseEntity.ok(productDTOs);
    }
}

