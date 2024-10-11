package com.ecommerce.core.repository;

import com.ecommerce.core.entity.Country;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {
}

