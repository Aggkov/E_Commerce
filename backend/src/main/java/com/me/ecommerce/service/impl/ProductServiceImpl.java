package com.me.ecommerce.service.impl;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.mapper.ProductMapper;
import com.me.ecommerce.repository.ProductRepository;
import com.me.ecommerce.service.ProductService;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import static com.me.ecommerce.utils.AppConstants.CREATED_AT;

// ProductServiceImpl.java
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public PagedResponse<ProductDTO> getAllProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Product> products = productRepository.findAll(pageable);

        if (products.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(),
                    products.getNumber(), products.getSize(),
                    products.getTotalElements(),
                    products.getTotalPages()
//                    , productListPage.isLast()
            );
        }

        List<ProductDTO> productDTOS = products.getContent().stream()
                .map(productMapper::productToProductDTO)
                .toList();

        return new PagedResponse<>(productDTOS, products.getNumber(),
                products.getSize(), products.getTotalElements(),
                products.getTotalPages()
//               , albums.isLast()
        );

//        return new PageImpl<>(productListPage.getContent()
//                .stream()
//                .sorted(Comparator.comparing(Product::getId))
//                .map(productMapper::productToProductDTO)
//                .collect(Collectors.toList()),
//                pageable,
//                productListPage.getTotalElements());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::productToProductDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product product) {
        // Find the existing product by ID
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update the existing product with new values
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setUnitPrice(product.getUnitPrice());
        // Update other fields as necessary

        // Save and return the updated product
        return productRepository.save(existingProduct);
    }

    @Override
    public Page<ProductDTO> getProductsByCategory(Long id, Pageable pageable) {
        Page<Product> productsByCategory = productRepository.findByCategoryId(id, pageable);
        return new PageImpl<>(
                productsByCategory.getContent()
                        .stream()
                        .sorted(Comparator.comparing(Product::getId))
                        .map(productMapper::productToProductDTO)
                        .collect(Collectors.toList()),
                pageable,
                productsByCategory.getTotalElements());

    }

    @Override
    public Page<ProductDTO> searchProductByKeywordsPaginated(String keywords, Pageable pageable) {
        Page<Product> productsPage = productRepository.searchByKeywordsPaginated(keywords, pageable);
        List<ProductDTO> productDTOs = productsPage.getContent()
                .stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(productDTOs, pageable, productsPage.getTotalElements());
    }

    @Override
    public List<ProductDTO> searchProductByKeywords(String keywords) {
        List<Product> products = productRepository.searchByKeywords(keywords);
        return products.stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
    }

//    public Page<ProductDTO> searchByKeywords(String keywords
////            , Integer limit
//            , Pageable pageable) {
//
//        List<Product> productList = productRepository.searchByKeywords(keywords);
//
//        // Get the total number of items
//        int totalItems = productList.size();
//// Apply pagination to the list manually
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), totalItems);
//        List<Product> paginatedProducts = productList.subList(start, end);
//
////        if (isPaginated && pageable.isPaged()) {
////            // Apply pagination manually
////            int start = (int) pageable.getOffset();
////            int end = Math.min((start + pageable.getPageSize()), productList.size());
////            productList = productList.subList(start, end);
////        }
//
//        // Map to DTOs
//        List<ProductDTO> productDTOs = paginatedProducts.stream()
//                .sorted(Comparator.comparing(Product::getId))
//                .map(productMapper::productToProductDTO)
//                .collect(Collectors.toCollection(ArrayList::new));
//
//        // Return the page with the correct total number of elements
//        return new PageImpl<>(productDTOs, pageable, totalItems);
//    }
}

