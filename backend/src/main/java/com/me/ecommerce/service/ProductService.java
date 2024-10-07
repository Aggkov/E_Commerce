package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    PagedResponse<ProductDTO> getAllProductsPaginated(int page, int size);
    ProductDTO getProductById(UUID id);
    Product saveProduct(ProductDTO productDTO, MultipartFile imageFile) throws IOException;
    void deleteProduct(UUID id);
    Product updateProduct(UUID id, Product product);
    PagedResponse<ProductDTO> getProductsByCategoryIdPaginated(UUID id, int page , int size);
    PagedResponse<ProductDTO> searchProductByKeywordsPaginated(String keywords, int page ,int size);
    List<ProductDTO> searchProductByKeywords(String keywords);
    PagedResponse<ProductDTO> getFilteredProducts(UUID categoryId, String min_price, String max_price, String priceRange, List<String> nameFilters, int page, int size);
    ResponseEntity<byte[]> export(String type) throws Exception;
}
