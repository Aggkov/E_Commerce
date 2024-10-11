package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.response.ProductCategoryDTO;
import com.ecommerce.core.entity.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    ProductCategoryMapper INSTANCE = Mappers.getMapper(ProductCategoryMapper.class);

    // Simple mapping method
    ProductCategoryDTO productCategoryToProductCategoryDTO(ProductCategory productCategory);

    // Reverse mapping method
    ProductCategory ProductCategoryDTOToproductCategory(ProductCategoryDTO productCategoryDTO);
}
