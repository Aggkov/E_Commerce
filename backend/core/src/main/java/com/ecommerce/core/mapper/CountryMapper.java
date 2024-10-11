package com.ecommerce.core.mapper;

import com.ecommerce.core.dto.response.CountryDTO;
import com.ecommerce.core.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface CountryMapper {

        CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);

        // Simple mapping method
        CountryDTO countryToCountryDTO(Country country);
        // Reverse mapping method
        Country countryDTOToCountry(CountryDTO countryDTO);
}
