package com.ecommerce.core.service.impl;

import com.ecommerce.core.dto.response.CountryDTO;
import com.ecommerce.core.entity.Country;
import com.ecommerce.core.mapper.CountryMapper;
import com.ecommerce.core.repository.CountryRepository;
import com.ecommerce.core.service.CountryService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public List<CountryDTO> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        return countries.stream()
//                .sorted(Comparator.comparing(Country::getId))
                .map(countryMapper::countryToCountryDTO)
                .toList();
    }

    @Override
    public CountryDTO getCountryById(UUID id) {
        return countryRepository.findById(id)
                .map(countryMapper::countryToCountryDTO)
                .orElseThrow(() -> new RuntimeException("Country not found"));
    }

    @Override
    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public void deleteCountry(UUID id) {
        countryRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<Country> updateCountry(UUID id, Country country) {
        return null;
    }
}
