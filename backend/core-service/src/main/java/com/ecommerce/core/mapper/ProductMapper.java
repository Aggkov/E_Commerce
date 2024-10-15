package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.response.ProductDTO;
import com.ecommerce.core.dto.response.export.ExportProductDTO;
import com.ecommerce.core.entity.Product;
import com.ecommerce.core.exception.BadRequestException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@Mapper (componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
//    @Value("${file.upload-dir}")
    String uploadDir = System.getProperty("user.dir");

    // Simple mapping method
//    @Mapping(target = "imageUrl", source = "product", qualifiedByName = "imageUrlToBytes")
    @Mapping(target = "imageUrl", source = "product.imageUrl")
    @Mapping(target = "categoryId", source = "product.category.id")
    @Mapping(target = "categoryName", source = "product.category.categoryName")
    ProductDTO productToProductDTO(Product product);

    @Mapping(target = "categoryId", source = "product.category.id")
    @Mapping(target = "categoryName", source = "product.category.categoryName")
    ExportProductDTO productToExportProductDTO(Product product);

    // Reverse mapping method
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "category.id", ignore = true)
    @Mapping(target = "category.categoryName", ignore = true)
    Product productDTOToProduct(ProductDTO productDTO);

    //    @Mapping(target = "imageUrl", source = "product.imageUrl")
    default byte[] mapImageUrl(String imageUrl) {
        if (StringUtils.isBlank(imageUrl)) {
            return null;
        }
        try {
//            System.out.println("Current working directory: " + System.getProperty("user.dir"));
            Path filePath = Paths.get(uploadDir, imageUrl);
            if (!Files.exists(filePath)) {
                throw new BadRequestException("File does not exist in this path", HttpStatus.BAD_REQUEST);
            }
            return Files.readAllBytes(filePath);
        }
        catch (IOException e) {
            System.out.println("IO error occured: " + imageUrl);
            return null;
        }
    }

    // Custom method to convert imageUrl to byte[] using MapStruct
//    @Named("imageUrlToBytes")
//    default String imageUrlToBytes(Product product) {
//        if (product == null || StringUtils.isBlank(product.getImageUrl())) {
//            return null;
//        }
//        try {
//            // Print the current working directory
////            System.out.println("Current directory: " + System.getProperty("user.dir"));
//            Path filePath = new File(uploadDir + "/" + product.getImageUrl()).toPath();
//            return Base64.getEncoder().encodeToString(Files.readAllBytes(filePath));
//        } catch (IOException e) {
//            // Handle error or log it if needed
//            System.out.println("File not found in the path: " + product.getImageUrl());
//            return null;
//        }
//    }
}

