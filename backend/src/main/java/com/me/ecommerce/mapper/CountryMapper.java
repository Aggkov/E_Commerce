package com.me.ecommerce.mapper;

import com.me.ecommerce.dto.response.CountryDTO;
import com.me.ecommerce.entity.Country;
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
