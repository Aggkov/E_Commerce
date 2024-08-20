package com.me.ecommerce.repository;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.utils.SearchUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class EntityManagerProductRepositoryImpl implements EntityManagerProductRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Product> searchByKeywordsPaginated(String keywords, Pageable pageable) {
        String[] tokens = SearchUtility.tokenize(keywords);
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

        List<Product> paginatedResult = query.getResultList();

        return new PageImpl<>(paginatedResult, pageable, totalRows);
    }

    @Override
    public List<Product> searchByKeywords(String keywords) {
        String[] tokens = SearchUtility.tokenize(keywords);
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
}
