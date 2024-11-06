package com.ecommerce.payment.repository;

import com.ecommerce.payment.entity.Payment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaypalRepository extends JpaRepository<Payment, UUID> {

}
