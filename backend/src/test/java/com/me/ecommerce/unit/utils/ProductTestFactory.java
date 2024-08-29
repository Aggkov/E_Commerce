package com.me.ecommerce.unit.utils;

import com.me.ecommerce.entity.Product;
import com.me.ecommerce.entity.ProductCategory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProductTestFactory {

    public static Set<Product> createProducts(ProductCategory category) {
        Set<Product> products = new LinkedHashSet<>();

        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setSku("BOOK-TECH-1000");
        product1.setName("Crash Course in Python");
        product1.setDescription("Learn Python at your own pace...");
        product1.setUnitPrice(new BigDecimal("14.99"));
        product1.setImageUrl("assets/images/products/books/book-1000.png");
        product1.setActive(true);
        product1.setUnitsInStock(100);
        product1.setUnitsSold(0);
        product1.setCategory(category);
        category.getProducts().add(product1);
        products.add(product1);

        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setSku("BOOK-TECH-1001");
        product2.setName("Become a Guru in JavaScript");
        product2.setDescription("Learn JavaScript at your own pace...");
        product2.setUnitPrice(new BigDecimal("20.99"));
        product2.setImageUrl("assets/images/products/books/book-1001.png");
        product2.setActive(true);
        product2.setUnitsInStock(100);
        product2.setUnitsSold(0);
        product2.setCategory(category);
        category.getProducts().add(product2);
        products.add(product2);

        Product product3 = new Product();
        product3.setId(UUID.randomUUID());
        product3.setSku("BOOK-TECH-1002");
        product3.setName("Exploring Vue.js");
        product3.setDescription("Learn Vue.js at your own pace...");
        product3.setUnitPrice(new BigDecimal("14.99"));
        product3.setImageUrl("assets/images/products/books/book-1002.png");
        product3.setActive(true);
        product3.setUnitsInStock(100);
        product3.setUnitsSold(0);
        product3.setCategory(category);
        category.getProducts().add(product3);
        products.add(product3);

        Product product4 = new Product();
        product4.setId(UUID.randomUUID());
        product4.setSku("BOOK-TECH-1003");
        product4.setName("Advanced Techniques in Big Data");
        product4.setDescription("Learn Big Data at your own pace...");
        product4.setUnitPrice(new BigDecimal("13.99"));
        product4.setImageUrl("assets/images/products/books/book-1003.png");
        product4.setActive(true);
        product4.setUnitsInStock(100);
        product4.setUnitsSold(0);
        product4.setCategory(category);
        category.getProducts().add(product4);
        products.add(product4);

        return products;

//        Product product = new Product();
//        product.setId(UUID.randomUUID());
//        product.setSku("BOOK-TECH-1004");
//        product.setName("Crash Course in Big Data");
//        product.setDescription("Learn Big Data at your own pace...");
//        product.setUnitPrice(new BigDecimal("18.99"));
//        product.setImageUrl("assets/images/products/books/book-1004.png");
//        product.setActive(true);
//        product.setUnitsInStock(100);
//        product.setUnitsSold(0);
//        product.setCategory(category);
//        return product;
//
//
//        Product product = new Product();
//        product.setId(UUID.randomUUID());
//        product.setSku("BOOK-TECH-1005");
//        product.setName("JavaScript Cookbook");
//        product.setDescription("Learn JavaScript at your own pace...");
//        product.setUnitPrice(new BigDecimal("23.99"));
//        product.setImageUrl("assets/images/products/books/book-1005.png");
//        product.setActive(true);
//        product.setUnitsInStock(100);
//        product.setUnitsSold(0);
//        product.setCategory(category);
//        return product;

    }
}


