package com.me.ecommerce.mapper;

import com.me.ecommerce.dto.response.ProductCategoryDTO;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.entity.ProductCategory;
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
