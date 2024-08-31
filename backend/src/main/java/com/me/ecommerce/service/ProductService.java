package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    PagedResponse<ProductDTO> getAllProducts(int page, int size);
    ProductDTO getProductById(UUID id);
    Product saveProduct(ProductDTO productDTO);
    void deleteProduct(UUID id);
    Product updateProduct(UUID id, Product product);
    PagedResponse<ProductDTO> getProductsByCategoryIdPaginated(UUID id, int page , int size);
    PagedResponse<ProductDTO> searchProductByKeywordsPaginated(String keywords, int page ,int size);
    List<ProductDTO> searchProductByKeywords(String keywords);
}
