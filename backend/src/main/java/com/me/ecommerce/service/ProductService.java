package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    PagedResponse<ProductDTO> getAllProducts(Integer page, Integer size);
    ProductDTO getProductById(Long id);
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    Product updateProduct(Long id, Product product);
    PagedResponse<ProductDTO> getProductsByCategory(Long id, Integer page ,Integer size);
    PagedResponse<ProductDTO> searchProductByKeywordsPaginated(String keywords, Integer page ,Integer size);
    List<ProductDTO> searchProductByKeywords(String keywords);
}
