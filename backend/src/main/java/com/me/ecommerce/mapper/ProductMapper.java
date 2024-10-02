package com.me.ecommerce.mapper;

import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper (componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // Simple mapping method
    // @Mapping(target = "categoryId", ignore = true) // Ignoring categoryId
    @Mapping(target = "categoryId", source = "product.category.id")
    @Mapping(target = "categoryName", source = "product.category.categoryName")
    ProductDTO productToProductDTO(Product product);

    // Reverse mapping method
    Product productDTOToProduct(ProductDTO productDTO);
}

