package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.response.ProductDTO;
import com.ecommerce.core.dto.response.export.ExportProductDTO;
import com.ecommerce.core.entity.Product;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;

@Mapper (componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // Simple mapping method
    @Mapping(target = "imageUrl", source = "product", qualifiedByName = "imageUrlToBytes")
    @Mapping(target = "categoryId", source = "product.category.id")
    @Mapping(target = "categoryName", source = "product.category.categoryName")
    ProductDTO productToProductDTO(Product product);

    @Mapping(target = "categoryId", source = "product.category.id")
    @Mapping(target = "categoryName", source = "product.category.categoryName")
    ExportProductDTO productToExportProductDTO(Product product);

    // Reverse mapping method
    @Mapping(target = "imageUrl", ignore = true)
    Product productDTOToProduct(ProductDTO productDTO);

    // Custom method to convert imageUrl to byte[] using MapStruct
    @Named("imageUrlToBytes")
    default byte[] imageUrlToBytes(Product product) {
        if (product == null || StringUtils.isBlank(product.getImageUrl())) {
            return null;
        }
        try {
            // Print the current working directory
//            System.out.println("Current directory: " + System.getProperty("user.dir"));
            Path filePath = new File("./backend/core-service/" + product.getImageUrl()).toPath();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            // Handle error or log it if needed
            System.out.println("File not found in the path: " + product.getImageUrl());
            return null;
        }
    }
}

