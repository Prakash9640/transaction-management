package com.microservices.transactionmanagement.repo;

import com.microservices.transactionmanagement.entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository
        extends JpaRepository<CustomerOrder, Long> {
}