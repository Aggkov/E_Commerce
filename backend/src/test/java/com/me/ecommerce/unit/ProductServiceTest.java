//package com.me.ecommerce.unit;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//import com.me.ecommerce.dto.response.PagedResponse;
//import com.me.ecommerce.dto.response.ProductDTO;
//import com.me.ecommerce.entity.Product;
//import com.me.ecommerce.mapper.ProductMapper;
//import com.me.ecommerce.repository.ProductRepository;
//import com.me.ecommerce.service.ProductService;
//import java.util.ArrayList;
//import java.util.Set;
//import java.util.UUID;
//import java.util.stream.Stream;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import java.util.List;
//
//@SpringBootTest
//public class ProductServiceTest {
//
//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private ProductMapper productMapper;
//
//    Set<Product> products;
//    private static final String CREATED_AT = "createdAt";
//
//    // to have clean data before each test
//    @BeforeEach
//    void setUp() {
//       ProductCategory category = new ProductCategory();
//       category.setId(UUID.fromString("e302d1b4-8609-417d-9f5a-ceb1fb1a9331"));
//       category.setCategoryName("Books");
//        products = ProductTestFactory.createProducts(category);
//    }
//
//   @ParameterizedTest
//   @MethodSource("pageableProvider")
//    void testGetAllProducts(Pageable pageable) {
//        Page<Product> productPage = productRepository.findAll(pageable);
//        List<ProductDTO> productDTOS = productPage.stream()
//                .map(productMapper::productToProductDTO)
//                .toList();
//
//        // Act
//        PagedResponse<ProductDTO> response = productService.getAllProducts(
//                pageable.getPageNumber(),
//                pageable.getPageSize()
//        );
//
//        assertNotNull(response);
//        assertEquals(productDTOS.size(), response.getContent().size());
//        assertEquals(pageable.getPageNumber(), response.getPage());
//        assertEquals(pageable.getPageSize(), response.getSize());
//        assertEquals(productPage.getTotalElements(), response.getTotalElements());
//        assertEquals(productPage.getTotalPages(), response.getTotalPages());
//    }
//
//    @Test
//    void testGetProductsByCategoryId() {
//        testGetByCategoryAndPage(UUID.fromString("e302d1b4-8609-417d-9f5a-ceb1fb1a9331"),
//                PageRequest.of(0, 1,
//                Sort.by(Sort.Direction.DESC, CREATED_AT)));
//
//        testGetByCategoryAndPage(UUID.fromString("e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32"),
//                PageRequest.of(0, 2,
//                        Sort.by(Sort.Direction.DESC, CREATED_AT)));
//
//        testGetByCategoryAndPage(UUID.fromString("b26be512-510f-4064-bf9c-32d525fe76aa"),
//                PageRequest.of(1, 2,
//                        Sort.by(Sort.Direction.DESC, CREATED_AT)));
//    }
//
//    public void testGetByCategoryAndPage(UUID categoryId, Pageable pageable) {
//        // Fetch products directly from the repository using actual database data
//        Page<Product> productsByCategoryPage = productRepository.findByCategoryId(categoryId, pageable);
//        List<ProductDTO> productDTOS = productsByCategoryPage.stream()
//                .map(productMapper::productToProductDTO)
//                .toList();
//
//        // Call the service method under test
//        PagedResponse<ProductDTO> response = productService.getProductsByCategoryIdPaginated(
//                categoryId,
//                pageable.getPageNumber(),
//                pageable.getPageSize());
//
//        assertNotNull(response);
//        assertEquals(productDTOS.size(), response.getContent().size());
//        assertEquals(pageable.getPageNumber(), response.getPage());
//        assertEquals(pageable.getPageSize(), response.getSize());
//        assertEquals(productsByCategoryPage.getTotalElements(), response.getTotalElements());
//        assertEquals(productsByCategoryPage.getTotalPages(), response.getTotalPages());
//    }
//
//    @ParameterizedTest
//    @MethodSource("pageableProvider")
//    void testSearchProductByKeywordsPaginated(Pageable pageable) {
//        Page<Product> productsByKeywordsPage = new PageImpl<>(new ArrayList<>(products),
//                pageable,
//                products.size());
//        List<ProductDTO> productDTOs = productsByKeywordsPage.stream()
//                .map(productMapper::productToProductDTO)
//                .toList();
//
//    }
//
//    static Stream<Pageable> pageableProvider() {
//        return Stream.of(
//                PageRequest.of(0, 1,
//                        Sort.by(Sort.Direction.DESC, "createdAt")),
//                PageRequest.of(0, 2,
//                        Sort.by(Sort.Direction.DESC, "createdAt")),
//                PageRequest.of(1, 2,
//                        Sort.by(Sort.Direction.DESC, "createdAt"))
//        );
//    }
//
//    static Stream<Arguments> pageableAndCategoryIdProvider() {
//        return Stream.of(
//                Arguments.of(
//                        UUID.fromString("e302d1b4-8609-417d-9f5a-ceb1fb1a9331"),
//                        PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt"))
//                ),
//                Arguments.of(
//                        UUID.fromString("e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32"),
//                        PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "createdAt"))
//                ),
//                Arguments.of(
//                        UUID.fromString("b26be512-510f-4064-bf9c-32d525fe76aa"),
//                        PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "createdAt"))
//                ),
//                Arguments.of(
//                        UUID.fromString("8e9f6773-c946-462d-afc5-69ffac311b10"),
//                        PageRequest.of(2, 3, Sort.by(Sort.Direction.DESC, "createdAt"))
//                )
//        );
//    }
//}
