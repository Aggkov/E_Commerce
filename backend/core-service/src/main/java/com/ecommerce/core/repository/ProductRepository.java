package com.ecommerce.core.repository;

import com.ecommerce.core.entity.Product;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    /*
    Instead of fetching the ProductCategory lazily and encountering this error,
    you can modify the query to use a JOIN FETCH. This way,
    the ProductCategory will be fetched together with the Product,
    preventing the LazyInitializationException.
     */
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.category.id = :id ORDER BY p.createdAt DESC")
    Page<Product> findByCategoryIdOrderByCreatedAt(@Param("id") UUID id, Pageable pageable);

    Product findByNameAndSku(String name, String sku);


//    @Query("SELECT p FROM Product p " +
//            "WHERE p.unitPrice " +
//            "BETWEEN :minPrice AND :maxPrice " +
//            "AND (:nameFilters IS NULL OR p.name IN :nameFilters) " +
////          "AND (COALESCE(:nameFilters, NULL) IS NULL OR p.name IN (:nameFilters))" +
//            "AND p.category.id = :categoryId " +
//            "ORDER BY p.createdAt DESC")
//    Page<Product> findProductsBetweenMinPriceAndMaxPrice(UUID categoryId, Double minPrice, Double maxPrice, List<String> nameFilters, Pageable pageable);

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

//    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY p.id")
//    Page<Product> findByNameContainingIgnoreCaseOrderById(@Param("name") String name, Pageable pageable);
}
/*
Methods Inherited from CrudRepository:
save(S entity): Saves a given entity. Returns the saved entity.
saveAll(Iterable<S> entities): Saves all given entities.
findById(ID id): Retrieves an entity by its ID. Returns an Optional<T>.
existsById(ID id): Checks whether an entity with the given ID exists.
findAll(): Returns all entities.
findAllById(Iterable<ID> ids): Returns all entities with the given IDs.
count(): Returns the number of entities.
deleteById(ID id): Deletes the entity with the given ID.
delete(T entity): Deletes a given entity.
deleteAllById(Iterable<? extends ID> ids): Deletes all entities with the given IDs.
deleteAll(Iterable<? extends T> entities): Deletes all given entities.
deleteAll(): Deletes all entities.
 */
