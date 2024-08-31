//package com.me.ecommerce.integration;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.me.ecommerce.configuration.TestDatabaseConfig;
//import com.me.ecommerce.dto.response.PagedResponse;
//import com.me.ecommerce.dto.response.ProductDTO;
//import com.me.ecommerce.entity.Product;
//import com.me.ecommerce.mapper.CountryMapperImpl;
//import com.me.ecommerce.mapper.ProductMapper;
//import com.me.ecommerce.repository.ProductRepository;
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//import java.util.UUID;
//import java.util.logging.Logger;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
    These integration tests should use Spring Boot Testcontainers instead of h2
 */

//@SpringBootTest()
//@ActiveProfiles("test")
//@Sql(value = "classpath:data.sql", executionPhase = BEFORE_TEST_METHOD)
//@AutoConfigureMockMvc
//class ProductControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private ProductMapper productMapper;
//
//    private static final Logger LOGGER = Logger.getLogger(ProductControllerIntegrationTest.class.getName());
//
//
//    private static final String CREATED_AT = "createdAt";
//
//
//    @ParameterizedTest
//    @MethodSource("pageableProvider")
//    void testGetAllProducts(Pageable pageable) throws Exception {
//        MvcResult mvcResult = mockMvc.perform(get(
//                "/api/products")
//                .param("page", String.valueOf(pageable.getPageNumber()))
//                .param("size", String.valueOf(pageable.getPageSize())))
//                .andExpect(status().isOk())
//                // This assertion checks that the JSON content in the body
//                // of the response is not empty.
//                .andExpect(jsonPath("$").isNotEmpty())
//                .andReturn();
//
//        String jsonResponse = mvcResult.getResponse().getContentAsString();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        PagedResponse<ProductDTO> response = objectMapper.readValue(
//                jsonResponse, new TypeReference<PagedResponse<ProductDTO>>(){}
//        );
//
//        assertNotNull(response);
//        assertNotNull(response.getContent());
//        assertEquals(pageable.getPageSize(), response.getSize());
//        assertEquals(pageable.getPageNumber(), response.getPage());
//        assertEquals(6, response.getTotalElements());
//        assertEquals((int) Math.ceil((double) response.getTotalElements() / response.getSize()), response.getTotalPages());
//    }
//
//    @Test
//    void testGetProductsByCategoryIdPaginated() throws Exception {
//        testGetByCategoryAndPage(UUID.fromString("e302d1b4-8609-417d-9f5a-ceb1fb1a9331"),
//                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, CREATED_AT)));
//
//        testGetByCategoryAndPage(UUID.fromString("e302d1b4-8609-417d-9f5a-ceb1fb1a9331"),
//                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, CREATED_AT)));
//
//        testGetByCategoryAndPage(UUID.fromString("e302d1b4-8609-417d-9f5a-ceb1fb1a9331"),
//                PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, CREATED_AT)));
//    }
//
//
//    private void testGetByCategoryAndPage(UUID categoryId, Pageable pageable) throws Exception {
//        MvcResult mvcResult = mockMvc.perform(get(
//                "/api/products/category/{categoryId}", categoryId)
//                .param("page", String.valueOf(pageable.getPageNumber()))
//                .param("size", String.valueOf(pageable.getPageSize()))
//                .param("sort", "createdAt,DESC"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String jsonResponse = mvcResult.getResponse().getContentAsString();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        PagedResponse<ProductDTO> response = objectMapper.readValue(
//                jsonResponse, new TypeReference<PagedResponse<ProductDTO>>(){}
//        );
//
//        // Assert the response body
//        assertNotNull(response);
//        assertNotNull(response.getContent());
//        assertEquals(pageable.getPageSize(), response.getSize());
//        assertEquals(pageable.getPageNumber(), response.getPage());
//
//        // not working since pagination is involved
////        List<String> expectedSkus = List.of("BOOK-TECH-1001","BOOK-TECH-1009"
////                ,"BOOK-TECH-1014","BOOK-TECH-1015","BOOK-TECH-1019", "BOOK-TECH-1021");
////        List<String> actualSkus = response.getContent().stream()
////                .map(ProductDTO::getSku)
////                .toList();
////        assertEquals(expectedSkus.size(), actualSkus.size());
////        assertTrue(actualSkus.containsAll(expectedSkus));
//
//        // Compare the results with what the repository returns
//        Page<Product> productsByCategoryPage = productRepository.findByCategoryId(categoryId, pageable);
//        List<ProductDTO> expectedProductDTOS = productsByCategoryPage.stream()
//                .map(productMapper::productToProductDTO)
//                .toList();
//
//        LOGGER.info("Expected ProductDTOs: " + expectedProductDTOS);
//        LOGGER.info("Actual ProductDTOs: " + response.getContent());
//
//        assertEquals(expectedProductDTOS.size(), response.getContent().size());
//        assertTrue(response.getContent().containsAll(expectedProductDTOS));
//        assertEquals(productsByCategoryPage.getTotalElements(), response.getTotalElements());
//        assertEquals(productsByCategoryPage.getTotalPages(), response.getTotalPages());
//
//    }
//
////    @Test
////    public void testSearchProductByKeywordsPaginated_StatusOk() throws Exception {
////        mockMvc.perform(get("/api/products/search/paginated")
////                        .param("keywords", "Java")
////                        .param("page", "0")
////                        .param("size", "10"))
////                .andExpect(status().isOk())
////                // This assertion checks that the JSON content in the body
////                // of the response is not empty.
////                .andExpect(jsonPath("$").isNotEmpty());
////    }
//
//    @Test
//    public void testSearchProductByKeywordsPaginated() throws Exception {
//        MvcResult mvcResult = mockMvc.perform(get("/api/products/search/paginated")
//                        .param("keywords", "guru java")
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String jsonResponse = mvcResult.getResponse().getContentAsString();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        PagedResponse<ProductDTO> response = objectMapper.readValue(
//                jsonResponse, new TypeReference<PagedResponse<ProductDTO>>(){}
//        );
//
//        List<String> expectedSkus = List.of("BOOK-TECH-1001", "BOOK-TECH-1021");
//        List<String> actualSkus = response.getContent().stream()
//                .map(ProductDTO::getSku)
//                .toList();
//
//        assertEquals(expectedSkus.size(), actualSkus.size());
//        assertTrue(actualSkus.containsAll(expectedSkus));
//    }
//
////    @Test
////    void testCreateProduct() {
////        // Step 1: Prepare the ProductDTO
////        ProductDTO productDTO = new ProductDTO();
////        productDTO.setName("Test Product");
////        productDTO.setSku("TEST-SKU-123");
////        productDTO.setDescription("This is a test product.");
////        productDTO.setPrice(BigDecimal.valueOf(19.99));
////        productDTO.setCategoryId(1L); // Assuming a valid category ID is 1
////    }
//
//    // Provide Pageable configurations for testing
//    static Stream<Pageable> pageableProvider() {
//        return Stream.of(
//                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, CREATED_AT)),
//                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, CREATED_AT)),
//                PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, CREATED_AT))
//        );
//    }
//}
//

//package com.me.ecommerce.unit;
//

//@SpringJUnitConfig
//class ProductControllerUnitTest {
//



