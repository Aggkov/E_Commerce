package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.CountryDTO;
import com.me.ecommerce.entity.Country;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

public interface CountryService {

    List<CountryDTO> getAllCountries();

    CountryDTO getCountryById(Short id);

    Country saveCountry(Country country);

    void deleteCountry(Short id);

    ResponseEntity<Country> updateCountry(Short id, Country country);

//    ResponseEntity<Country> updateCountry(Short id, Country country);
}
