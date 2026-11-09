package com.microservices.transactionmanagement.service;

import com.microservices.transactionmanagement.entity.Account;
import com.microservices.transactionmanagement.repo.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DebitService {

    private final AccountRepository accountRepository;

    @Transactional
    public String debit(Long accountId, BigDecimal amount) {

        Account account = accountRepository
                .findAccountForUpdate(accountId)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        BigDecimal balance = account.getBalance();

        System.out.println(
                Thread.currentThread().getName()
                        + " Read balance: " + balance);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        if (balance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(balance.subtract(amount));

        return "Debit successful";
    }
}