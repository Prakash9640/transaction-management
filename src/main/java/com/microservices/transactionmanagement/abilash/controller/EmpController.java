package com.microservices.transactionmanagement.abilash.controller;

import com.microservices.transactionmanagement.abilash.entity.Employee;
import com.microservices.transactionmanagement.abilash.service.EmpService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class EmpController {
    private final EmpService empService;
    public EmpController(EmpService empService) {
        this.empService = empService;
    }

    @GetMapping("/save-employee/{simulateException}")
    @Transactional
    public String saveEmployeeInfo(@PathVariable boolean simulateException) {
        try
        {
            for(long i=1;i<10;i++){
            empService.saveEmployeeInfo(i, new Employee("employee"+i), simulateException);
        }
        }catch (Exception e){
            System.out.println("Exception occurred while saving employee information: " + e.getMessage());
        }
        return "Employee information saved successfully.";
    }
}
