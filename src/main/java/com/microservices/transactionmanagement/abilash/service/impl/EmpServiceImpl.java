package com.microservices.transactionmanagement.abilash.service.impl;

import com.microservices.transactionmanagement.abilash.entity.Employee;
import com.microservices.transactionmanagement.abilash.repo.EmpRepo;
import com.microservices.transactionmanagement.abilash.service.EmpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpServiceImpl implements EmpService {

    private final EmpRepo empRepo;
    public EmpServiceImpl(EmpRepo empRepo) {
        this.empRepo = empRepo;
    }
    @Override
    @Transactional
    public void saveEmployeeInfo(Long id, Employee employee, boolean simulateException) {
        System.out.println("Saving employees info: "+ id);
        empRepo.save(employee);
        if(simulateException) {
            throw new RuntimeException("Simulating an exception after saving employee info");
        }
        System.out.println("Employees info saved successfully: "+ id);
    }
}
