package com.ecommerce.core.repository;

import com.ecommerce.core.entity.BillingAddress;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, UUID> {

    @Query("SELECT a FROM BillingAddress a WHERE a.id = :id OR " +
            "(LOWER(a.street) = LOWER(:street) AND " +
            "LOWER(a.zipCode) = LOWER(:zipCode) AND " +
            "LOWER(a.city) = LOWER(:city) AND " +
            "a.state.id = :stateId)")
    Optional<BillingAddress> findBillingAddressByIdOrStreetAndZipCodeAndCityAndState(
            @Param("id") UUID id,
            @Param("street") String street,
            @Param("zipCode") String zipCode,
            @Param("city") String city,
            @Param("stateId") UUID stateId);
}
