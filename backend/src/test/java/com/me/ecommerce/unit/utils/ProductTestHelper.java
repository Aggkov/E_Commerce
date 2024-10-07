package com.me.ecommerce.unit.utils;

import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.entity.ProductCategory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductTestHelper {

    public static List<Product> createMockProducts() {
        ProductCategory category = new ProductCategory();
        category.setId(UUID.fromString("e302d1b4-8609-417d-9f5a-ceb1fb1a9331"));
        category.setCategoryName("Books");

        List<Product> products = new ArrayList<>();

        products.add(createProduct(
                UUID.fromString("7a759e5a-1f49-4379-8064-e697e073e7ee"),
                "BOOK-TECH-1001",
                "Become a Guru in JavaScript",
                "Learn JavaScript at your own pace.",
                "assets/images/products/books/book-1001.png",
                new BigDecimal("20.99"),
                100,
                0,
                category
        ));

        products.add(createProduct(
                UUID.fromString("1a3d726c-4e38-4f85-9856-5a481eac4589"),
                "BOOK-TECH-1009",
                "Become a Guru in React.js",
                "Learn React.js at your own pace.",
                "assets/images/products/books/book-1009.png",
                new BigDecimal("23.99"),
                100,
                0,
                category
        ));

        products.add(createProduct(
                UUID.fromString("dc8999c3-c41c-4146-92c5-bf38d9014193"),
                "BOOK-TECH-1014",
                "Introduction to SQL",
                "Learn SQL at your own pace.",
                "assets/images/products/books/book-1014.png",
                new BigDecimal("22.99"),
                100,
                0,
                category
        ));

        products.add(createProduct(
                UUID.fromString("db054956-8376-47d5-9e38-0422493c6c3a"),
                "BOOK-TECH-1015",
                "The Expert Guide to JavaScript",
                "Learn JavaScript at your own pace.",
                "assets/images/products/books/book-1015.png",
                new BigDecimal("22.99"),
                100,
                0,
                category
        ));

        products.add(createProduct(
                UUID.fromString("8725ec9a-efe8-4f14-8aab-8e40d79ea976"),
                "BOOK-TECH-1019",
                "Crash Course in JavaScript",
                "Learn JavaScript at your own pace.",
                "assets/images/products/books/book-1019.png",
                new BigDecimal("13.99"),
                100,
                0,
                category
        ));

        products.add(createProduct(
                UUID.fromString("f0016ed4-9664-49a2-8578-b1df2f092b16"),
                "BOOK-TECH-1021",
                "Become a Guru in Java",
                "Learn Java at your own pace.",
                "assets/images/products/books/book-1021.png",
                new BigDecimal("18.99"),
                100,
                0,
                category
        ));

        return products;
    }

    private static Product createProduct(UUID id, String sku, String name, String description,
                                         String imageUrl, BigDecimal unitPrice, int unitsInStock,
                                         Integer unitsSold, ProductCategory category) {
        Product product = new Product();
        product.setId(id);
        product.setSku(sku);
        product.setName(name);
        product.setDescription(description);
        product.setImageUrl(imageUrl);
        product.setUnitPrice(unitPrice);
        product.setUnitsInStock(unitsInStock);
        product.setUnitsSold(unitsSold);
        product.setCategory(category);
        product.setActive(true); // Assuming all products are active

        return product;
    }

    public static ProductDTO mapProductToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setSku(product.getSku());
        productDTO.setName(product.getName());
        productDTO.setUnitPrice(product.getUnitPrice());
        productDTO.setUnitsInStock(product.getUnitsInStock());
//        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setDescription(product.getDescription());

        return productDTO;
    }
}

