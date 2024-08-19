package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    PagedResponse<ProductDTO> getAllProducts(Integer page, Integer size);
    ProductDTO getProductById(Long id);
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    Product updateProduct(Long id, Product product);
    Page<ProductDTO> getProductsByCategory(Long id, Pageable pageable);
    Page<ProductDTO> searchProductByKeywordsPaginated(String keywords, Pageable pageable);
    List<ProductDTO> searchProductByKeywords(String keywords);
}
