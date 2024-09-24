package com.me.ecommerce.repository;
import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.utils.SearchUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class EntityManagerProductRepositoryImpl implements EntityManagerProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Product> searchByKeywordsPaginated(String keywords, Pageable pageable) {
//        String[] tokens = SearchUtility.tokenizeWords(keywords);
        char[] tokens = SearchUtility.tokenizeCharacters(keywords);
        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM Product p WHERE ");

        for (int i = 0; i < tokens.length; i++) {
            queryBuilder.append("LOWER(p.name) LIKE LOWER(CONCAT('%', :token").append(i).append(", '%'))");
            if (i < tokens.length - 1) {
                queryBuilder.append(" AND ");
            }
        }
        queryBuilder.append(" ORDER BY p.id");

//        When you use EntityManager.createQuery, you are typically creating a JPQL query.
        TypedQuery<Product> query = entityManager.createQuery(queryBuilder.toString(), Product.class);
        for (int i = 0; i < tokens.length; i++) {
            query.setParameter("token" + i, tokens[i]);
        }

        int totalRows = query.getResultList().size();
        // pageable.getOffset() gives you the index of the first item on the current page.
        query.setFirstResult((int) pageable.getOffset());
        // pageable.getPageSize() returns the number of items per page.
        query.setMaxResults(pageable.getPageSize());

        List<Product> products = query.getResultList();

        return new PageImpl<>(products, pageable, totalRows);
    }

    @Override
    public List<Product> searchByKeywords(String keywords) {
//        String[] tokens = SearchUtility.tokenizeWords(keywords);
        char[] tokens = SearchUtility.tokenizeCharacters(keywords);
        StringBuilder queryBuilder = new StringBuilder("SELECT p FROM Product p WHERE ");

        for (int i = 0; i < tokens.length; i++) {
            queryBuilder.append("LOWER(p.name) LIKE LOWER(CONCAT('%', :token").append(i).append(", '%'))");
            if (i < tokens.length - 1) {
                queryBuilder.append(" AND ");
            }
        }
        queryBuilder.append(" ORDER BY p.id");
        queryBuilder.append(" LIMIT 10");

//        When you use EntityManager.createQuery, you are typically creating a JPQL query.
        TypedQuery<Product> query = entityManager.createQuery(queryBuilder.toString(), Product.class);
        for (int i = 0; i < tokens.length; i++) {
            query.setParameter("token" + i, tokens[i]);
        }
        return query.getResultList();
    }

    @Override
//    @Transactional(readOnly = true)
    public Page<Product> getFilteredProducts(UUID categoryId, Double minPrice, Double maxPrice, List<String> nameFiltersList, Pageable pageable) {
        // Use Criteria API to build dynamic query
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        // Basic conditions
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.between(root.get("unitPrice"), minPrice, maxPrice));
        predicates.add(cb.equal(root.get("category").get("id"), categoryId));

        // Add name filter conditions if nameFiltersList is not empty
        if (nameFiltersList != null && !nameFiltersList.isEmpty()) {
            List<Predicate> namePredicates = new ArrayList<>();
            for (String filter : nameFiltersList) {
                namePredicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.toLowerCase() + "%"));
            }
            predicates.add(cb.or(namePredicates.toArray(new Predicate[0])));
        }

        // Final query setup
        query.select(root).where(cb.and(predicates.toArray(new Predicate[0]))).orderBy(cb.desc(root.get("createdAt")));
        List<Product> resultList = entityManager.createQuery(query).getResultList();

        return new PageImpl<>(resultList, pageable, resultList.size());
    }
}
