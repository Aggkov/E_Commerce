package com.me.ecommerce.unit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.entity.ProductCategory;
import com.me.ecommerce.mapper.ProductMapper;
import com.me.ecommerce.repository.ProductRepository;
import com.me.ecommerce.service.impl.ProductServiceImpl;
import com.me.ecommerce.unit.utils.ProductTestFactory;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {

    /*
    is a mock object. When used in the test, it does not interact with the actual
    ProductRepository implementation but instead responds based on how you've configured
    it in the test.
     */
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    /*
    The @InjectMocks annotation is used to create an instance of the class under test and
    automatically inject the mocks (created with @Mock) into it.
    This allows you to test the class under test with mocked dependencies.
     */
    @InjectMocks
    private ProductServiceImpl productService;
    List<Product> products;
//    private Product product1;
//    private Product product2;
    private static final String CREATED_AT = "createdAt";

    // to have clean data before each test
    @BeforeEach
    void setUp() {
        ProductCategory category = new ProductCategory();
        category.setId(UUID.fromString("e302d1b4-8609-417d-9f5a-ceb1fb1a9331"));
        category.setCategoryName("Books");
        products = ProductTestFactory.createProducts(category);
    }

    @Test
    public void testGetAllProducts_NoProductsFound() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, CREATED_AT);
        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());

        // mock (fake)
        when(productRepository.findAll(pageable)).thenReturn(emptyPage);

        // test actual method, but mock product service defined above will be called
        PagedResponse<ProductDTO> response = productService.getAllProducts(0, 10);

        // Assert
        assertTrue(response.getContent().isEmpty());
        assertEquals(0, response.getPage());
        assertEquals(0, response.getSize());
        assertEquals(0, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
    }

    @ParameterizedTest
    @MethodSource("pageableProvider")
    void testGetAllProductsByPageNumberAndPageSize(Pageable pageable) {
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());
        List<ProductDTO> productDTOS = products.stream()
                .map(productMapper::productToProductDTO)
                .toList();

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // Act
        PagedResponse<ProductDTO> response = productService.getAllProducts(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        // Assert
        assertEquals(productDTOS.size(), response.getContent().size());
        assertEquals(pageable.getPageNumber(), response.getPage());
        assertEquals(pageable.getPageSize(), response.getSize());
        assertEquals(products.size(), response.getTotalElements());
        assertEquals((int) Math.ceil((double) products.size() / pageable.getPageSize()),
                response.getTotalPages());
    }

    static Stream<Pageable> pageableProvider() {
        return Stream.of(
                PageRequest.of(0, 1,
                        Sort.by(Sort.Direction.DESC, "createdAt")),
                PageRequest.of(0, 2,
                        Sort.by(Sort.Direction.DESC, "createdAt")),
                PageRequest.of(1, 2,
                        Sort.by(Sort.Direction.DESC, "createdAt"))
        );
    }
}
