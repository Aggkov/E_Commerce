package com.me.ecommerce.service.impl;

import com.me.ecommerce.dto.request.FilterCriteria;
import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.mapper.ProductMapper;
import com.me.ecommerce.repository.ProductRepository;
import com.me.ecommerce.service.ProductService;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import static com.me.ecommerce.utils.AppConstants.CREATED_AT;

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
    public PagedResponse<ProductDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Product> productsPage = productRepository.findAll(pageable);

        if (productsPage.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(),
                    productsPage.getNumber(), productsPage.getSize(),
                    productsPage.getTotalElements(),
                    productsPage.getTotalPages()
//                    , products.isLast()
            );
        }
        List<ProductDTO> productDTOs = productsPage.getContent().stream()
                .map(productMapper::productToProductDTO)
                .toList();
        return new PagedResponse<>(productDTOs, productsPage.getNumber(),
                productsPage.getSize(), productsPage.getTotalElements(),
                productsPage.getTotalPages()
//               , products.isLast()
        );
    }

    public PagedResponse<ProductDTO> getProductsByCategoryIdPaginated(UUID id, int page , int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Product> productsByCategoryPage = productRepository.findByCategoryIdOrderByCreatedAt(id, pageable);

        List<ProductDTO> productDTOS = productsByCategoryPage.getContent().stream()
                .map(productMapper::productToProductDTO)
                .toList();
        return new PagedResponse<>(productDTOS, productsByCategoryPage.getNumber(),
                productsByCategoryPage.getSize(), productsByCategoryPage.getTotalElements(),
                productsByCategoryPage.getTotalPages()
//               , albums.isLast()
        );
    }

    @Override
    public PagedResponse<ProductDTO> searchProductByKeywordsPaginated(String keywords, int page ,int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Product> productsByKeywordsPage = productRepository.searchByKeywordsPaginated(keywords, pageable);

        List<ProductDTO> productDTOs = productsByKeywordsPage.getContent().stream()
                .map(productMapper::productToProductDTO)
                .toList();
        return new PagedResponse<>(productDTOs, productsByKeywordsPage.getNumber(),
                productsByKeywordsPage.getSize(), productsByKeywordsPage.getTotalElements(),
                productsByKeywordsPage.getTotalPages()
//               , albums.isLast()
        );
    }

    @Override
    public PagedResponse<ProductDTO> getFilteredProducts(
            UUID categoryId, String min_price, String max_price, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
        Page<Product> productPage = productRepository.findProductsBetweenMinPriceAndMaxPrice(
                categoryId,
                Double.parseDouble(min_price),
                Double.parseDouble(max_price),
                pageable);
        List<ProductDTO> productDTOS = productPage.getContent().stream()
                .map(productMapper::productToProductDTO)
                .toList();

        return new PagedResponse<>(productDTOS, productPage.getNumber(), productPage.getSize(),
                productPage.getTotalElements(), productPage.getTotalPages());
    }

    @Override
    public List<ProductDTO> searchProductByKeywords(String keywords) {
        List<Product> products = productRepository.searchByKeywords(keywords);
        return products.stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::productToProductDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product saveProduct(ProductDTO productDTO) {
        Product product = productMapper.productDTOToProduct(productDTO);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(UUID id, Product product) {
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
}

