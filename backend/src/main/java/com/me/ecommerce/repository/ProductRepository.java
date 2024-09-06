package com.me.ecommerce.repository;

import com.me.ecommerce.entity.Product;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, UUID>, EntityManagerProductRepository {
    // behind the scenes select ... where category_id = id will be called
    Page<Product> findByCategoryId(@Param("id") UUID id, Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE p.unitPrice " +
            "BETWEEN :minPrice AND :maxPrice " +
            "AND p.category.id = :categoryId " +
            "ORDER BY p.createdAt")
    Page<Product> findProductsBetweenMinPriceAndMaxPrice(UUID categoryId, Double minPrice, Double maxPrice, Pageable pageable);

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
