package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    PagedResponse<ProductDTO> getAllProducts(int page, int size);
    ProductDTO getProductById(UUID id);
    Product saveProduct(Product product);
    void deleteProduct(UUID id);
    Product updateProduct(UUID id, Product product);
    PagedResponse<ProductDTO> getProductsByCategory(UUID id, int page ,int size);
    PagedResponse<ProductDTO> searchProductByKeywordsPaginated(String keywords, int page ,int size);
    List<ProductDTO> searchProductByKeywords(String keywords);
}
