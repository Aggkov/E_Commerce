package com.me.ecommerce.repository.criteria;

import com.me.ecommerce.entity.Product;
import com.me.ecommerce.utils.SearchUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CriteriaProductRepositoryImpl implements CriteriaProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Product> searchByKeywordsPaginated(String keywords, Pageable pageable) {

//      String[] tokens = SearchUtility.tokenizeWords(keywords);
        char[] tokens = SearchUtility.tokenizeCharacters(keywords);

        // Initialize CriteriaBuilder and CriteriaQuery
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        // Prepare a list of predicates to store conditions
        List<Predicate> predicates = new ArrayList<>();

        // Create LIKE predicates dynamically for each token
        for (char token : tokens) {
            // Create a predicate for each token to search in the name field
            Predicate predicate = cb.like(cb.lower(root.get("name")), "%" + Character.toLowerCase(token) + "%");
            predicates.add(predicate);
        }

        // Combine predicates using AND
        query.select(root).where(cb.and(predicates.toArray(new Predicate[0]))).orderBy(cb.asc(root.get("createdAt")));

        // Create the query
        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        int totalRows = typedQuery.getResultList().size();

        // set pagination
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        // Execute query and get the result list
        List<Product> products = typedQuery.getResultList();

        // Return the results as a pageable response
        return new PageImpl<>(products, pageable, totalRows);

        // Get the total count of matching results
//       CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//       countQuery.select(cb.count(countQuery.from(Product.class)))
//               .where(cb.and(predicates.toArray(new Predicate[0])));
//       Long totalRows = entityManager.createQuery(countQuery).getSingleResult();

        // Return the results as a pageable response
//        return new PageImpl<>(products, pageable, totalRows);

        // Stringbuilder
//        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM Product p WHERE ");
//
//        for (int i = 0; i < tokens.length; i++) {
//            queryBuilder.append("LOWER(p.name) LIKE LOWER(CONCAT('%', :token").append(i).append(", '%'))");
//            if (i < tokens.length - 1) {
//                queryBuilder.append(" AND ");
//            }
//        }
//        queryBuilder.append(" ORDER BY p.createdAt");
//
////        When you use EntityManager.createQuery, you are typically creating a JPQL query.
//        TypedQuery<Product> query = entityManager.createQuery(queryBuilder.toString(), Product.class);
//        for (int i = 0; i < tokens.length; i++) {
//            query.setParameter("token" + i, tokens[i]);
//        }
//        // total number of products
//        int totalRows = query.getResultList().size();
//
//        // pageable.getOffset() gives you the index of the first item on the current page.
//        query.setFirstResult((int) pageable.getOffset());
//        // pageable.getPageSize() returns the number of items per page.
//        query.setMaxResults(pageable.getPageSize());
//
//        // total elements after performing pagination
//        List<Product> products = query.getResultList();
//        // pass paginated elements to PageImpl<>()
//        return new PageImpl<>(products, pageable, totalRows);
    }

    /*
    this works brings back some nice results non page
     */
    @Override
    public List<Product> searchByKeywords(String keywords) {
//        String[] tokens = SearchUtility.tokenizeWords(keywords);
        char[] tokens = SearchUtility.tokenizeCharacters(keywords);
        // Initialize CriteriaBuilder and CriteriaQuery
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        // Prepare a list of predicates to store conditions
        List<Predicate> predicates = new ArrayList<>();

        // Create LIKE predicates dynamically for each token
        for (char token : tokens) {
            // Create a predicate for each token to search in the name field
            Predicate predicate = cb.like(cb.lower(root.get("name")), "%" + Character.toLowerCase(token) + "%");
            predicates.add(predicate);
        }


        // Combine predicates using AND
        query.select(root).where(cb.and(predicates.toArray(new Predicate[0]))).orderBy(cb.asc(root.get("createdAt")));

        // Create the query and set pagination parameters
        TypedQuery<Product> typedQuery = entityManager.createQuery(query);

        // Set the limit to 10 results
        typedQuery.setMaxResults(10);

        return typedQuery.getResultList();


//        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM Product p WHERE ");
//
//        for (int i = 0; i < tokens.length; i++) {
//            queryBuilder.append("LOWER(p.name) LIKE LOWER(CONCAT('%', :token").append(i).append(", '%'))");
//            if (i < tokens.length - 1) {
//                queryBuilder.append(" AND ");
//            }
//        }
//        queryBuilder.append(" ORDER BY p.createdAt");
//        queryBuilder.append(" LIMIT 10");
//
////        When you use EntityManager.createQuery, you are typically creating a JPQL query.
//        TypedQuery<Product> query = entityManager.createQuery(queryBuilder.toString(), Product.class);
//        for (int i = 0; i < tokens.length; i++) {
//            query.setParameter("token" + i, tokens[i]);
//        }
//        return query.getResultList();
    }

    @Override
// @Transactional(readOnly = true) // Optional: Marks the method as read-only, optimizing transaction handling
    public Page<Product> getFilteredProducts(UUID categoryId, Double minPrice, Double maxPrice, List<String> nameFiltersList, Pageable pageable) {
            // Step 1: Initialize CriteriaBuilder and CriteriaQuery
            // The CriteriaBuilder is used to construct the query programmatically.
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            // Create a query object that will return Product entities.
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            // Define the root of the query (equivalent to FROM Product p in SQL).
            Root<Product> root = query.from(Product.class);

            // Step 2: Create a list to hold the conditions (predicates) for the query
            // This list will store all the WHERE conditions that will be applied.
            List<Predicate> predicates = new ArrayList<>();

            // Step 3: Add basic conditions to the predicates list
            // Filter products within the specified price range.
            predicates.add(cb.between(root.get("unitPrice"), minPrice, maxPrice));
            // Filter products by category ID.
            predicates.add(cb.equal(root.get("category").get("id"), categoryId));

            // Step 4: Add name filter conditions if the nameFiltersList is not empty
            if (Objects.nonNull(nameFiltersList) && !nameFiltersList.isEmpty()) {
                // Create a list of LIKE conditions for each filter in the nameFiltersList.
                List<Predicate> namePredicates = new ArrayList<>();
                for (String filter : nameFiltersList) {
                    // Create a LIKE condition for each filter, checking if the product name contains the filter value.
                    namePredicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.toLowerCase() + "%"));
                }
                // Combine all name predicates using OR (at least one name filter should match).
                predicates.add(cb.or(namePredicates.toArray(new Predicate[0])));
            }

            // Step 5: Combine all predicates using AND and set the order of the results
            // Apply the predicates to the query and order the results by createdAt in descending order.
            query.select(root).where(cb.and(predicates.toArray(new Predicate[0]))).orderBy(cb.desc(root.get("createdAt")));

            // Step 6: Create a TypedQuery object to execute the criteria query
            // Prepare the query to be executed against the database.
            TypedQuery<Product> typedQuery = entityManager.createQuery(query);

            // Step 7: Get the total number of matching rows before applying pagination
            // This step fetches all matching products and counts them to determine the total number of rows.
            // It is needed to provide the correct total count for the pagination response.
            int totalRows = typedQuery.getResultList().size();

            // Step 8: Set pagination parameters
            // Set the starting index for the first result to fetch (based on the current page).
            typedQuery.setFirstResult((int) pageable.getOffset());
            // Limit the number of results to the page size (number of items per page).
            typedQuery.setMaxResults(pageable.getPageSize());

            // Step 9: Execute the paginated query and fetch the results
            // This will return only the results for the current page.
            List<Product> products = typedQuery.getResultList();

            // Step 10: Return the paginated results as a PageImpl object
            // The PageImpl object takes the current page results, pagination info, and total number of rows.
            return new PageImpl<>(products, pageable, totalRows);
        }
    }

