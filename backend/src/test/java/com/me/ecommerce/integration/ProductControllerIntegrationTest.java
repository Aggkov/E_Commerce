package com.me.ecommerce.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.mapper.ProductMapper;
import com.me.ecommerce.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest()
@ActiveProfiles("test")
@Sql(value = "classpath:data.sql", executionPhase = BEFORE_TEST_METHOD)
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    private static final String CREATED_AT = "createdAt";


    @Test
    public void testSearchProductByKeywordsPaginated_StatusOkAndNotNullResponse() throws Exception {
        mockMvc.perform(get("/api/products/search/paginated")
                        .param("keywords", "Java")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                // This assertion checks that the JSON content in the body
                // of the response is not empty.
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testSearchProductByKeywordsPaginated_CorrectProductsReturned() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/products/search/paginated")
                        .param("keywords", "guru java")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        PagedResponse<ProductDTO> response = objectMapper.readValue(
                jsonResponse, new TypeReference<PagedResponse<ProductDTO>>(){}
        );

        List<String> expectedSkus = List.of("BOOK-TECH-1001", "BOOK-TECH-1021");
        List<String> actualSkus = response.getContent().stream()
                .map(ProductDTO::getSku)
                .toList();

        assertEquals(expectedSkus.size(), actualSkus.size());
        assertTrue(actualSkus.containsAll(expectedSkus));
    }

    @ParameterizedTest
    @MethodSource("pageableProvider")
    void testGetAllProducts(Pageable pageable) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/products")
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                // This assertion checks that the JSON content in the body
                // of the response is not empty.
                .andExpect(jsonPath("$").isNotEmpty())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        PagedResponse<ProductDTO> response = objectMapper.readValue(
                jsonResponse, new TypeReference<PagedResponse<ProductDTO>>(){}
        );

        assertNotNull(response);
        assertNotNull(response.getContent());
        assertEquals(pageable.getPageSize(), response.getSize());
        assertEquals(pageable.getPageNumber(), response.getPage());
        assertEquals(6, response.getTotalElements());
        assertEquals((int) Math.ceil((double) response.getTotalElements() / response.getSize()), response.getTotalPages());
    }
//
//    @Test
//    void testGetProductsByCategoryId() {
//        testGetByCategoryAndPage(UUID.fromString("e302d1b4-8609-417d-9f5a-ceb1fb1a9331"),
//                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, CREATED_AT)));
//
//        testGetByCategoryAndPage(UUID.fromString("e74d708e-b0c7-42b1-bb7d-b42f7b2b0b32"),
//                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, CREATED_AT)));
//
//        testGetByCategoryAndPage(UUID.fromString("b26be512-510f-4064-bf9c-32d525fe76aa"),
//                PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, CREATED_AT)));
//    }
//
//    private void testGetByCategoryAndPage(UUID categoryId, Pageable pageable) {
//        // Build the URI with pagination and sorting parameters
//        String uri = UriComponentsBuilder.fromPath("/api/products/category/{categoryId}")
//                .queryParam("page", pageable.getPageNumber())
//                .queryParam("size", pageable.getPageSize())
//                .queryParam("sort", "createdAt,DESC")
//                .buildAndExpand(categoryId.toString())
//                .toUriString();
//
//        // Send the GET request
//        ResponseEntity<PagedResponse<ProductDTO>> responseEntity =
//                restTemplate.exchange(
//                uri,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<>() {
//                }
//        );
//
//        // Assert that the status is OK
//        assertEquals(200, responseEntity.getStatusCode().value());
//
//        // Assert the response body
//        PagedResponse<ProductDTO> response = responseEntity.getBody();
//        assertNotNull(response);
//        assertEquals(pageable.getPageSize(), response.getSize());
//        assertEquals(pageable.getPageNumber(), response.getPage());
//        assertNotNull(response.getContent());
//
//        // Compare the results with what the repository returns
//        Page<Product> productsByCategoryPage = productRepository.findByCategoryId(categoryId, pageable);
//        List<ProductDTO> expectedProductDTOS = productsByCategoryPage.stream()
//                .map(productMapper::productToProductDTO)
//                .toList();
//
//        assertEquals(expectedProductDTOS.size(), response.getContent().size());
//        assertEquals(productsByCategoryPage.getTotalElements(), response.getTotalElements());
//        assertEquals(productsByCategoryPage.getTotalPages(), response.getTotalPages());
//
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

