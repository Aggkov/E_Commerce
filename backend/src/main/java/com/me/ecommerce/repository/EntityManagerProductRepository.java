package com.me.ecommerce.repository;
import com.me.ecommerce.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntityManagerProductRepository {
    List<Product> searchByKeywords(String keywords);
    Page<Product> searchByKeywordsPaginated(String keywords, Pageable pageable);
}
