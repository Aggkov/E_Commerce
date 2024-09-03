package com.me.ecommerce.repository;

import com.me.ecommerce.entity.ShippingAddress;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, UUID> {

    @Query("SELECT a FROM ShippingAddress a WHERE a.id = :id OR " +
            "(LOWER(a.street) = LOWER(:street) AND " +
            "LOWER(a.zipCode) = LOWER(:zipCode) AND " +
            "LOWER(a.city) = LOWER(:city) AND " +
            "a.state.id = :stateId)")
    Optional<ShippingAddress> findShippingAddressByIdOrStreetAndZipCodeAndCityAndState(
            @Param("id") UUID id,
            @Param("street") String street,
            @Param("zipCode") String zipCode,
            @Param("city") String city,
            @Param("stateId") UUID stateId);
}
