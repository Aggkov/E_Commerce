package com.me.ecommerce.repository;

import com.me.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {
}
