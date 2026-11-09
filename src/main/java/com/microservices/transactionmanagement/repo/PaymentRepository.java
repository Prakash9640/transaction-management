package com.microservices.transactionmanagement.repo;

import com.microservices.transactionmanagement.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
