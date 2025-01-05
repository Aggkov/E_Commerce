package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.response.ProductDTO;
import com.ecommerce.core.dto.response.ProductDTOAdminView;
import com.ecommerce.core.dto.response.export.ExportProductDTO;
import com.ecommerce.core.entity.Product;
import com.ecommerce.core.exception.BadRequestException;
import com.ecommerce.core.service.ProductService;
import com.ecommerce.core.service.impl.ProductServiceImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring")
public interface ProductMapper {

//    ProductService product = new ProductServiceImpl();

    //    @Mapping(target = "imageUrl", source = "product", qualifiedByName = "imageUrlToBytes")
    @Mapping(target = "imageUrl", source = "product.imageUrl")
    @Mapping(target = "categoryId", source = "product.category.id")
    @Mapping(target = "categoryName", source = "product.category.categoryName")
    ProductDTO productToProductDTO(Product product
//            , @Context String uploadDir // pass external parameter from service layer, in service inject properties value
    );


    ProductDTOAdminView productToProductDTOAdminView(Product product);

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
            // Docker
//            Path filePath = Paths.get("core-service", imageUrl);

            // Local Development
            String currentWorkingDir = System.getProperty("user.dir");
            Path filePath;
            // means we are in root so go to core
            if (!currentWorkingDir.endsWith("core-service")) {
                // Move two levels down
                Path coreDir = Paths.get(currentWorkingDir, "backend", "core-service");
                filePath = Paths.get(coreDir.toString(), imageUrl);
            } else {
                filePath = Paths.get(System.getProperty("user.dir"), imageUrl);
            }
            if (!Files.exists(filePath)) {
                throw new BadRequestException("File does not exist in this path", HttpStatus.BAD_REQUEST);
            }
            return Files.readAllBytes(filePath);

        } catch (IOException e) {
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
//            String currentWorkingDir = System.getProperty("user.dir");
//            Path filePath;
//            // If the working directory ends with "core-service", move up to the root directory
//            if (currentWorkingDir.endsWith("core-service")) {
//                // Move two levels up to the project root
//                Path rootDir = Paths.get(currentWorkingDir).getParent().getParent();
//                filePath = Paths.get(rootDir.toString(), imageUrl);
//            } else {
//                filePath = Paths.get(System.getProperty("user.dir"), imageUrl);
//            }
//            if (!Files.exists(filePath)) {
//                throw new BadRequestException("File does not exist in this path", HttpStatus.BAD_REQUEST);
//            }
//            return Files.readAllBytes(filePath);
//        }
//        catch (IOException e) {
//            System.out.println("IO error occured: " + imageUrl);
//            return null;
//        }
}

