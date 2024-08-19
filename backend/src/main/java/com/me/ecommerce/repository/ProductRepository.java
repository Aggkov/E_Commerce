package com.me.ecommerce.repository;

import com.me.ecommerce.entity.Product;
import com.me.ecommerce.entity.ProductCategory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin("http://localhost:4200")
public interface ProductRepository extends JpaRepository<Product, Long>, EntityManagerProductRepository {
    /*
        Spring data gives these by default
    POST /products create new
    GET  /products  show all
    GET  /products/{id} get by id
    PUT  /products/{id} update by id
    DELETE  /products/{id} delete by id
     */

//    @Query("SELECT p FROM Product p ORDER BY p.id")
//    List<Product> findAllOrderById();

//    @Query("SELECT p FROM Product p WHERE p.category = :category ORDER BY p.id")
//    List<Product> findByCategoryOrderById(@Param("category") ProductCategory productCategory);

    // behind the scenes select ... where category_id = id will be called
    Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);

     @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY p.id")
    Page<Product> findByNameContainingIgnoreCaseOrderById(@Param("name") String name, Pageable pageable);

}
