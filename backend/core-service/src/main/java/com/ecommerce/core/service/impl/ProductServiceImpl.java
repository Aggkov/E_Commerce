package com.ecommerce.core.service.impl;

import com.ecommerce.core.dto.response.PagedResponse;
import com.ecommerce.core.dto.response.ProductDTO;
import com.ecommerce.core.dto.response.ProductDTOAdminView;
import com.ecommerce.core.dto.response.export.ExportProductDTO;
import com.ecommerce.core.utils.Helper;
import com.ecommerce.core.entity.Product;
import com.ecommerce.core.entity.ProductCategory;
import com.ecommerce.core.exception.BadRequestException;
import com.ecommerce.core.exception.ResourceNotFoundException;
import com.ecommerce.core.mapper.ProductMapper;
import com.ecommerce.core.repository.ProductCategoryRepository;
import com.ecommerce.core.repository.criteria.CriteriaProductRepository;
import com.ecommerce.core.repository.ProductRepository;
import com.ecommerce.core.service.ExportService;
import com.ecommerce.core.service.ProductService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;


import static com.ecommerce.core.utils.AppConstants.CREATED_AT;

@Service
@RequiredArgsConstructor
//@NoArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CriteriaProductRepository criteriaProductRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductMapper productMapper;

    @Override
    public PagedResponse<ProductDTOAdminView> getAllProductsPaginated(int page, int size) {
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
        List<ProductDTOAdminView> productDTOs = productsPage.getContent().stream()
                .map(productMapper::productToProductDTOAdminView)
                .toList();
        return new PagedResponse<>(productDTOs, productsPage.getNumber(),
                productsPage.getSize(), productsPage.getTotalElements(),
                productsPage.getTotalPages()
//               , products.isLast()
        );
    }

    public PagedResponse<ProductDTO> getProductsByCategoryIdPaginated(UUID id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Product> productsByCategoryPage = productRepository.findByCategoryIdOrderByCreatedAt(id, pageable);
        if (productsByCategoryPage.getContent().isEmpty()) {
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
    public PagedResponse<ProductDTO> searchProductByKeywordsPaginated(String keywords, int page, int size) {
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

        try {
            // Adjust price range if selectedPriceRange is set
            double minPrice = Double.parseDouble(min_price);
            double maxPrice = Double.parseDouble(max_price);
            if (Objects.nonNull(priceRange) && !priceRange.isEmpty()) {
                String[] range = priceRange.split("-");
                if (range.length == 2) {
                    minPrice = Math.max(minPrice, Double.parseDouble(range[0]));
                    maxPrice = Math.min(maxPrice, Double.parseDouble(range[1]));
                } else if ("20".equals(priceRange)) {
                    minPrice = Math.max(minPrice, 20);
                    maxPrice = minPrice;
                }
                if (maxPrice < minPrice) {
                    double temp = minPrice;
                    minPrice = maxPrice;
                    maxPrice = temp;
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

        } catch (NullPointerException | NumberFormatException exception) {
            Page<Product> productPage = criteriaProductRepository.getFilteredProducts(
                    categoryId,
                    10.0,
                    15.0,
                    nameFilters,
                    pageable);

            List<ProductDTO> productDTOS = productPage.getContent().stream()
                    .map(productMapper::productToProductDTO)
                    .toList();

            return new PagedResponse<>(productDTOS, productPage.getNumber(), productPage.getSize(),
                    productPage.getTotalElements(), productPage.getTotalPages());
        }
    }

    @Override
    public ResponseEntity<byte[]> export(String type) throws Exception {
        ExportService<ExportProductDTO> exportService = Helper.getExportService(type);
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

//        products.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
//        products.sort((p1, p2) -> Long.compare(p2.getCreatedAt().getEpochSecond(), p1.getCreatedAt().getEpochSecond()));
        List<ExportProductDTO> productDTOS = products.stream()
                .map(productMapper::productToExportProductDTO)
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
    public ProductDTO getProductByName(String name) {
        return productRepository.findByName(name)
                .map(productMapper::productToProductDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Product with name " + name + " was not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO, MultipartFile imageFile) throws IOException {
        Product product = productRepository.findByNameAndSku(productDTO.getName(), productDTO.getSku());

        if (Objects.nonNull(product)) {
            throw new BadRequestException(
                    "Product with " + productDTO.getName() + " and " + productDTO.getSku() + " already exists",
                    HttpStatus.BAD_REQUEST);
        }

        ProductCategory category = productCategoryRepository.findByCategoryName(productDTO.getCategoryName()).orElseThrow(
                () -> new ResourceNotFoundException("category with this ID was not found", HttpStatus.NOT_FOUND)
        );
        // Get the correct upload directory based on the category
        String uploadDir = getUploadDir(productDTO.getCategoryName());
        if (!uploadDir.isEmpty() && Files.exists(Paths.get(uploadDir)) && !imageFile.isEmpty()) {
            // Save the image to the correct directory
            String fileName = imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, imageFile.getBytes());
        } else {
            throw new BadRequestException("Upload directory: " + uploadDir + " not found",
                    HttpStatus.BAD_REQUEST);
        }
        product = productMapper.productDTOToProduct(productDTO);
        product.setCategory(category);
        category.getProducts().add(product);

        // convert to database URI
        int index = uploadDir.indexOf("uploads");
        String dbUploadDir = uploadDir.substring(index);

        product.setImageUrl(dbUploadDir + "/"
                + imageFile.getOriginalFilename());
        productRepository.save(product);
        ProductDTO productDTo = ProductDTO.builder()
                .sku(product.getSku())
                .categoryName(product.getCategory().getCategoryName())
                .name(product.getName()).build();
        return productDTo;
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

    // A utility method to determine the correct upload directory
    private String getUploadDir(String categoryName) throws BadRequestException {
        // Docker
//        return switch (categoryName.toLowerCase()) {
//            case "books" -> "/core-service/uploads/images/books";
//            case "coffee mugs" -> "/core-service/uploads/images/coffeemugs";
//            case "luggage tags" -> "/core-service/uploads/images/luggagetags";
//            case "mouse pads" -> "/core-service/uploads/images/mousepads";
//            default -> throw new BadRequestException("Unknown category: " + categoryName, HttpStatus.BAD_REQUEST);
//        };

        // Local Dev
        String currentWorkingDir = System.getProperty("user.dir");
        if (!currentWorkingDir.endsWith("core-service")) {
            // go down
            currentWorkingDir = Paths.get(currentWorkingDir, "backend", "core-service").toString();
            // go up
//            currentWorkingDir = (Paths.get(currentWorkingDir).getParent().getParent()).toString();
        }
        switch (categoryName.toLowerCase()) {
            case "books":
                return currentWorkingDir + "/uploads/images/books";
            case "coffee mugs":
                return currentWorkingDir + "/uploads/images/coffeemugs";
            case "luggage tags":
                return currentWorkingDir + "/uploads/images/luggagetags";
            case "mouse pads":
                return currentWorkingDir + "/uploads/images/mousepads";
            default:
                throw new BadRequestException("Unknown category: " + categoryName, HttpStatus.BAD_REQUEST);
        }
    }
}

