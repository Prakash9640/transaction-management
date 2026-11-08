package com.microservices.transactionmanagement.abilash.repo;

import com.microservices.transactionmanagement.abilash.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface EmpRepo extends JpaRepository<Employee, Long> {
}
