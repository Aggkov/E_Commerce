package com.me.ecommerce.service.impl;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.entity.ProductCategory;
import com.me.ecommerce.exception.ResourceNotFoundException;
import com.me.ecommerce.mapper.ProductMapper;
import com.me.ecommerce.repository.ProductCategoryRepository;
import com.me.ecommerce.repository.criteria.CriteriaProductRepository;
import com.me.ecommerce.repository.ProductRepository;
import com.me.ecommerce.service.ExportService;
import com.me.ecommerce.service.ProductService;
import com.me.ecommerce.utils.Helper;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;


import static com.me.ecommerce.utils.AppConstants.CREATED_AT;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CriteriaProductRepository criteriaProductRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CriteriaProductRepository criteriaProductRepository, ProductCategoryRepository productCategoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.criteriaProductRepository = criteriaProductRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public PagedResponse<ProductDTO> getAllProductsPaginated(int page, int size) {
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
        if(productsByCategoryPage.getContent().isEmpty()) {
            throw new ResourceNotFoundException("Products not found for this category", HttpStatus.NOT_FOUND);
        }

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

        Page<Product> productsByKeywordsPage = criteriaProductRepository.searchByKeywordsPaginated(keywords, pageable);

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
            UUID categoryId, String min_price, String max_price, String priceRange, List<String> nameFilters, int page, int size) {
        // exceptions : category id is invalid, prices or (pages) bad request?
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        // Adjust price range if selectedPriceRange is set
        double minPrice = Double.parseDouble(min_price);
        double maxPrice = Double.parseDouble(max_price);
        if (priceRange != null && !priceRange.isEmpty()) {
            String[] range = priceRange.split("-");
            if (range.length == 2) {
                minPrice = Math.max(minPrice, Double.parseDouble(range[0]));
                maxPrice = Math.min(maxPrice, Double.parseDouble(range[1]));
            } else if (priceRange.equals("20")) {
                minPrice = Math.max(minPrice, 20);
            }
        }

        Page<Product> productPage = criteriaProductRepository.getFilteredProducts(
                categoryId,
                minPrice,
                maxPrice,
                nameFilters,
                pageable);

        List<ProductDTO> productDTOS = productPage.getContent().stream()
                .map(productMapper::productToProductDTO)
                .toList();

        return new PagedResponse<>(productDTOS, productPage.getNumber(), productPage.getSize(),
                productPage.getTotalElements(), productPage.getTotalPages());
    }

    @Override
    public ResponseEntity<byte[]> export(String type) throws Exception {
        ExportService<ProductDTO> exportService = Helper.getExportService(type);
        List<Product> products = productRepository.findAll();

        List<ProductDTO> productDTOS = products.stream()
                .map(productMapper::productToProductDTO)
                .toList();

        byte[] exportedData = exportService.export(productDTOS);
        HttpHeaders httpHeaders = new HttpHeaders();
        Helper.setHeaders(httpHeaders, type);
        // Return the file as a downloadable response
        return ResponseEntity.ok().headers(httpHeaders).body(exportedData);
    }

    @Override
    public List<ProductDTO> searchProductByKeywords(String keywords) {
        List<Product> products = criteriaProductRepository.searchByKeywords(keywords);
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
        ProductCategory category = productCategoryRepository.findById(UUID.fromString(productDTO.getCategoryId())).orElseThrow(
                () -> new ResourceNotFoundException("category with this ID was not found", HttpStatus.NOT_FOUND)
        );
        Product product = productMapper.productDTOToProduct(productDTO);
        product.setCategory(category);
        category.getProducts().add(product);
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

