package com.microservices.transactionmanagement.abilash.service;

import com.microservices.transactionmanagement.abilash.entity.Employee;

public interface EmpService {

     void saveEmployeeInfo(Long id, Employee employee, boolean simulateException);
}
