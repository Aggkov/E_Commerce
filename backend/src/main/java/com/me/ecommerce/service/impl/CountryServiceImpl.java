package com.me.ecommerce.service.impl;

import com.me.ecommerce.dto.response.CountryDTO;
import com.me.ecommerce.entity.Country;
import com.me.ecommerce.entity.ProductCategory;
import com.me.ecommerce.mapper.CountryMapper;
import com.me.ecommerce.repository.CountryRepository;
import com.me.ecommerce.service.CountryService;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                .sorted(Comparator.comparing(Country::getId))
                .map(countryMapper::countryToCountryDTO)
                .toList();
    }

    @Override
    public CountryDTO getCountryById(Short id) {
        return countryRepository.findById(id)
                .map(countryMapper::countryToCountryDTO)
                .orElseThrow(() -> new RuntimeException("Country not found"));
    }

    @Override
    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public void deleteCountry(Short id) {
        countryRepository.deleteById(id);
    }
}
