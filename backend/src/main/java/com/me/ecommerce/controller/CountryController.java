package com.me.ecommerce.controller;

import com.me.ecommerce.dto.response.ApiResponse;
import com.me.ecommerce.dto.response.CountryDTO;
import com.me.ecommerce.entity.Country;
import com.me.ecommerce.service.CountryService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value ="/api/countries", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200") // Replace with your frontend URL
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<List<CountryDTO>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> getCountryById(@PathVariable UUID id) {
        return ResponseEntity.ok(countryService.getCountryById(id));
    }

    @PostMapping
    public ResponseEntity<Country> createCountry(@RequestBody Country country) {
        return ResponseEntity.ok(countryService.saveCountry(country));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable UUID id, @RequestBody Country country) {
        return countryService.updateCountry(id, country);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse> deleteCountry(@PathVariable Short id) {
//        return countryService.deleteCountry(id);
//    }
}
