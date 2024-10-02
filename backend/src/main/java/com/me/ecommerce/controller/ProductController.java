package com.me.ecommerce.controller;

import com.me.ecommerce.dto.request.FilterCriteria;
import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.service.ProductService;
import com.me.ecommerce.utils.AppConstants;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "products", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200") // Replace with your frontend URL
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public PagedResponse<ProductDTO> getAllProducts(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size
    ) {
        return productService.getAllProductsPaginated(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/category/{id}")
    public PagedResponse<ProductDTO> getProductsByCategoryIdPaginated(
            @PathVariable UUID id,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return productService.getProductsByCategoryIdPaginated(id, page, size);
    }

    @GetMapping("/search/paginated")
    public PagedResponse<ProductDTO> searchProductByKeywordsPaginated(
            @RequestParam(name = "keywords") String keywords,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return productService.searchProductByKeywordsPaginated(keywords,
                page, size);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProductByKeywords(
            @RequestParam(name = "keywords") String keywords
    ) {
        List<ProductDTO> productDTOs = productService.searchProductByKeywords(keywords);
        return ResponseEntity.ok(productDTOs);
    }

    @PostMapping("/filter")
    public PagedResponse<ProductDTO> getFilteredProducts(@RequestBody FilterCriteria filterCriteria
    ) {
        return productService.getFilteredProducts(
                filterCriteria.getCategoryId(),
                filterCriteria.getMinPrice(),
                filterCriteria.getMaxPrice(),
                filterCriteria.getPriceRange(),
                filterCriteria.getNameFilters(),
                filterCriteria.getPage(),
                filterCriteria.getSize()
        );
    }

    // Only allow admin users to access this endpoint
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.saveProduct(productDTO));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(@RequestParam String type) throws Exception {
        return productService.export(type);



//        byte[] exportedData = exportService.export(mockData);
//
//        // Set the content type and headers based on the file type
//        HttpHeaders headers = new HttpHeaders();
//        switch (type.toLowerCase()) {
//            case "excel":
//                headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//                headers.set("Content-Disposition", "attachment; filename=export.xlsx");
//                break;
//            case "csv":
//                headers.setContentType(MediaType.parseMediaType("text/csv"));
//                headers.set("Content-Disposition", "attachment; filename=export.csv");
//                break;
//            case "json":
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                headers.set("Content-Disposition", "attachment; filename=export.json");
//                break;
//            case "yaml":
//                headers.setContentType(MediaType.parseMediaType("application/x-yaml"));
//                headers.set("Content-Disposition", "attachment; filename=export.yaml");
//                break;
//        }
//
//        return ResponseEntity.ok().headers(headers).body(exportedData);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
//        return productService.deleteProduct(id);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }
}

