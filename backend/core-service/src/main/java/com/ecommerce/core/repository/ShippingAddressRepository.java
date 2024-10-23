package com.ecommerce.core.repository;

import com.ecommerce.core.entity.ShippingAddress;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, UUID> {

    @Query("SELECT a FROM ShippingAddress a WHERE " +
            "(LOWER(a.street) = LOWER(:street) AND " +
            "LOWER(a.zipCode) = LOWER(:zipCode) AND " +
            "LOWER(a.city) = LOWER(:city) AND " +
            "a.state.id = :stateId)")
    Optional<ShippingAddress> findShippingAddressByStreetAndZipCodeAndCityAndState(
            @Param("street") String street,
            @Param("zipCode") String zipCode,
            @Param("city") String city,
            @Param("stateId") UUID stateId);

    @Query("SELECT sa " +
            "FROM ShippingAddress sa " +
            "JOIN sa.users user " +
            "JOIN FETCH sa.state " +
            "WHERE user.email = :email"
    )
    Optional<ShippingAddress> getShippingAddress(@Param("email") String email);
}
