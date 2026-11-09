package com.microservices.transactionmanagement.controller;


import com.microservices.transactionmanagement.dto.TransferRequest;
import com.microservices.transactionmanagement.service.TransferService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/payment")
public class AccountController {

    private final TransferService transferService;
    private static int count =1;

    public AccountController(TransferService transferService) {
        this.transferService = transferService;
    }


    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest transferRequest){

        String result = transferService.transferMoney(transferRequest.getFromAccountId(),
                transferRequest.getToAccountId(),
                transferRequest.getAmount(), count++);

        return ResponseEntity.ok(result);
    }


    @PostMapping("/{id}/debit")
    public String debit(
            @PathVariable Long id,
            @RequestParam BigDecimal amount) {

        return transferService.debit(id, amount);
    }
    @GetMapping("/test-concurrency")
    public String testConcurrency() {

        transferService.testConcurrentDebit();

        return "Two debit threads started";
    }
}
