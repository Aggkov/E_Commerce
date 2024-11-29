package com.ecommerce.core.unit;

import com.ecommerce.core.dto.response.ProductDTOAdminView;
import com.ecommerce.core.unit.utils.ProductTestHelper;
import com.ecommerce.core.dto.response.PagedResponse;
import com.ecommerce.core.dto.response.ProductDTO;
import com.ecommerce.core.entity.Product;
import com.ecommerce.core.entity.ProductCategory;
import com.ecommerce.core.mapper.ProductMapper;
import com.ecommerce.core.repository.criteria.CriteriaProductRepository;
import com.ecommerce.core.repository.ProductRepository;
import com.ecommerce.core.service.impl.ProductServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CriteriaProductRepository criteriaProductRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private MockMvc mockMvc;

    private static final String CREATED_AT = "createdAt";

    private List<Product> mockProducts;
    private ProductCategory mockCategory;

    @BeforeEach
    void setUp() {
        mockProducts = ProductTestHelper.createMockProducts();
        mockCategory = mockProducts.get(0).getCategory();
    }

    @ParameterizedTest
    @MethodSource("pageableProvider")
    void testGetAllProductsPaginated(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), mockProducts.size());
        Page<Product> mockPage = new PageImpl<>(
                mockProducts.subList(start, end),
                pageable,
                mockProducts.size());


        when(productRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        // Mock the mapping
        when(productMapper.productToProductDTO(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    return ProductTestHelper.mapProductToProductDTO(product);
                });
        PagedResponse<ProductDTOAdminView> response = productService.getAllProductsPaginated(
                pageable.getPageNumber(),
                pageable.getPageSize());

        assertNotNull(response);
        assertEquals(pageable.getPageSize(), response.getSize());
        assertEquals(pageable.getPageNumber(), response.getPage());
        assertEquals(mockProducts.size(), response.getTotalElements());
        assertEquals((int) Math.ceil((double) mockProducts.size() / response.getSize()), response.getTotalPages());
    }

    @Test
    void testGetProductsByCategoryIdPaginated() throws Exception {
        testGetByCategoryAndPage(mockCategory.getId(),
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, CREATED_AT)));

        testGetByCategoryAndPage(mockCategory.getId(),
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, CREATED_AT)));

        testGetByCategoryAndPage(mockCategory.getId(),
                PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, CREATED_AT)));
    }

    private void testGetByCategoryAndPage(UUID categoryId, Pageable pageable) throws Exception {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), mockProducts.size());
        Page<Product> mockPage = new PageImpl<>(
                mockProducts.subList(start, end),
                pageable,
                mockProducts.size());

        when(productRepository.findByCategoryIdOrderByCreatedAt(categoryId, pageable)).thenReturn(mockPage);

        when(productMapper.productToProductDTO(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    return ProductTestHelper.mapProductToProductDTO(product);
                });

        PagedResponse<ProductDTO> response = productService.getProductsByCategoryIdPaginated(
                categoryId,
                pageable.getPageNumber(),
                pageable.getPageSize());

        assertNotNull(response);
        assertEquals(pageable.getPageSize(), response.getSize());
        assertEquals(pageable.getPageNumber(), response.getPage());
        assertEquals(mockProducts.size(), response.getTotalElements());
        assertEquals((int) Math.ceil((double) mockProducts.size() / response.getSize()), response.getTotalPages());
    }

    @Test
    public void testSearchProductByKeywordsPaginated() {
        String keywords = "guru java";
        Pageable pageable = PageRequest.of(0,
                2,
                Sort.by(Sort.Direction.DESC, CREATED_AT));

        List<Product> filteredProducts = List.of(mockProducts.get(0),
                mockProducts.get(5)); // Only products with "guru java"

        Page<Product> mockPage = new PageImpl<>(
                filteredProducts,
                pageable,
                filteredProducts.size());

        when(criteriaProductRepository.searchByKeywordsPaginated(
                "guru java",
                pageable))
                .thenReturn(mockPage);

        // Mock the mapping
        when(productMapper.productToProductDTO(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    return ProductTestHelper.mapProductToProductDTO(product);
                });

        PagedResponse<ProductDTO> response =
                productService.searchProductByKeywordsPaginated(
                        keywords,
                        pageable.getPageNumber(),
                        pageable.getPageSize());

        assertNotNull(response);
        assertEquals(pageable.getPageSize(), response.getSize());
        assertEquals(pageable.getPageNumber(), response.getPage());
        assertEquals(mockPage.getContent().size(), response.getTotalElements());
        assertEquals((int) Math.ceil((double) mockPage.getContent().size() / response.getSize()), response.getTotalPages());

    }

    @Test
    void testGetProductById() {
        Product mockProduct = mockProducts.get(0);
        String productName = mockProduct.getName();

        when(productRepository.findByName(productName)).thenReturn(
                Optional.of(mockProduct)
        );

        when(productMapper.productToProductDTO(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    return ProductTestHelper.mapProductToProductDTO(product);
                });

        ProductDTO response = productService.getProductByName(productName);

        // test product found in mock with dto returned by service
        assertNotNull(response);
        assertEquals(mockProduct.getId(), response.getId());
        assertEquals(mockProduct.getSku(), response.getSku());
        assertEquals(mockProduct.getName(), response.getName());
        assertEquals(mockProduct.getDescription(), response.getDescription());
    }

//    @Test
//    void testSaveProduct() {
//        ProductDTO productDTO = new ProductDTO();
//        productDTO.setSku("TEST-SKU-123");
//        productDTO.setName("Test Product");
//        productDTO.setDescription("This is a test product.");
//
//        Product product = new Product();
//        product.setSku("TEST-SKU-123");
//        product.setName("Test Product");
//        product.setDescription("This is a test product.");
//
//        when(productMapper.productDTOToProduct(productDTO)).thenReturn(product);
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        Product savedProduct = productService.createProduct(productDTO);
//
//        // Verify that the mapper and repository
//        // methods were called with the correct arguments.
//        verify(productMapper).productDTOToProduct(productDTO);
//        verify(productRepository).save(product);
//
//        assertNotNull(savedProduct);
//        assertEquals(product.getSku(), savedProduct.getSku());
//        assertEquals(product.getName(), savedProduct.getName());
//        assertEquals(product.getDescription(), savedProduct.getDescription());
//    }

    // Provide Pageable configurations for testing
    static Stream<Pageable> pageableProvider() {
        return Stream.of(
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, CREATED_AT)),
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, CREATED_AT)),
                PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, CREATED_AT))
        );
    }
}
