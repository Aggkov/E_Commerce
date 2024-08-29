package com.me.ecommerce.integration;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.mapper.ProductMapper;
import com.me.ecommerce.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Optional: Specify a profile if you have a test-specific configuration
class ProductControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    private static final String CREATED_AT = "createdAt";

    @ParameterizedTest
    @MethodSource("pageableProvider")
    void testGetAllProducts(Pageable pageable) {
        // Build the URI with pagination parameters
        String uri = UriComponentsBuilder.fromPath("/api/products")
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .queryParam("sort", "createdAt,DESC")
                .toUriString();

        // Send the GET request
        ResponseEntity<PagedResponse<ProductDTO>> responseEntity =
                restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedResponse<ProductDTO>>() {
                }
        );

        // Assert that the status is OK
        assertEquals(200, responseEntity.getStatusCode().value());
        // Assert the response body
        PagedResponse<ProductDTO> response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(pageable.getPageSize(), response.getSize());
        assertEquals(pageable.getPageNumber(), response.getPage());
        assertNotNull(response.getContent());
        assertEquals(100, response.getTotalElements());
        assertEquals((int) Math.ceil((double) response.getTotalElements() / response.getSize()), response.getTotalPages());
    }

    @Test
    void testGetProductsByCategoryId() {
        testGetByCategoryAndPage(UUID.fromString("e302d1b4-8609-417d-9f5a-ceb1fb1a9331"),
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, CREATED_AT)));

        testGetByCategoryAndPage(UUID.fromString("e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32"),
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, CREATED_AT)));

        testGetByCategoryAndPage(UUID.fromString("b26be512-510f-4064-bf9c-32d525fe76aa"),
                PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, CREATED_AT)));
    }

    private void testGetByCategoryAndPage(UUID categoryId, Pageable pageable) {
        // Build the URI with pagination and sorting parameters
        String uri = UriComponentsBuilder.fromPath("/api/products/category/{categoryId}")
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .queryParam("sort", "createdAt,DESC")
                .buildAndExpand(categoryId.toString())
                .toUriString();

        // Send the GET request
        ResponseEntity<PagedResponse<ProductDTO>> responseEntity =
                restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        // Assert that the status is OK
        assertEquals(200, responseEntity.getStatusCode().value());

        // Assert the response body
        PagedResponse<ProductDTO> response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(pageable.getPageSize(), response.getSize());
        assertEquals(pageable.getPageNumber(), response.getPage());
        assertNotNull(response.getContent());

        // Compare the results with what the repository returns
        Page<Product> productsByCategoryPage = productRepository.findByCategoryId(categoryId, pageable);
        List<ProductDTO> expectedProductDTOS = productsByCategoryPage.stream()
                .map(productMapper::productToProductDTO)
                .toList();

        assertEquals(expectedProductDTOS.size(), response.getContent().size());
        assertEquals(productsByCategoryPage.getTotalElements(), response.getTotalElements());
        assertEquals(productsByCategoryPage.getTotalPages(), response.getTotalPages());

    }

    // Provide Pageable configurations for testing
    static Stream<Pageable> pageableProvider() {
        return Stream.of(
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, CREATED_AT)),
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, CREATED_AT)),
                PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, CREATED_AT))
        );
    }
}

