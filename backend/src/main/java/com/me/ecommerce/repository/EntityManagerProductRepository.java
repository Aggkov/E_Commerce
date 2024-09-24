package com.me.ecommerce.repository;
import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntityManagerProductRepository {
    List<Product> searchByKeywords(String keywords);
    Page<Product> searchByKeywordsPaginated(String keywords, Pageable pageable);
    Page<Product> getFilteredProducts(UUID categoryId, Double min_price, Double max_price,
                                                         List<String> nameFiltersList, Pageable pageable);
}
