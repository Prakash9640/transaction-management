package com.microservices.transactionmanagement.service;

import com.microservices.transactionmanagement.entity.Account;
import com.microservices.transactionmanagement.repo.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    private final DebitService debitService;

    public TransferService(AccountRepository accountRepository, DebitService debitService) {
        this.accountRepository = accountRepository;
        this.debitService = debitService;
    }

    //@Transactional
    public String transferMoney(Long fromAccount, Long toAccount, BigDecimal amount, int count) {

        //1. validate the accounts transfer
        if(fromAccount.equals(toAccount)){
            throw new IllegalArgumentException("Cannot transfer money to the same account");
        }

        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        // 2. Fetch and lock both accounts
        List<Account> accounts =
                accountRepository.findByAccountNumber(
                        Stream.of(fromAccount, toAccount)
                                .sorted()
                                .toList());

        Account fromAccountHolder = accounts.stream()
                .filter(account -> account.getId().equals(fromAccount))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("From account not found: " + fromAccount));


        Account toAccountHolder = accounts.stream()
                .filter(account -> account.getId().equals(toAccount))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("To account not found: " + toAccount));

        // 3. Check if the from account has sufficient balance
        BigDecimal balance = fromAccountHolder.getBalance();
        if (fromAccountHolder.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance in the from account: " + fromAccount);
        }

        fromAccountHolder.setBalance(balance.subtract(amount));
        // Simulate credit failure
        if(count!=3) {
            accountRepository.save(fromAccountHolder);
            throw new RuntimeException("Credit operation failed");
        }
        toAccountHolder.setBalance(toAccountHolder.getBalance().add(amount));
        // Logic to transfer money between accounts
        return "Transfer successful";
    }


    @Transactional
    public String debit(Long accountId, BigDecimal amount) {

        // Acquire database lock
        Account account = accountRepository
                .findAccountForUpdate(accountId)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        BigDecimal balance = account.getBalance();

        System.out.println(
                Thread.currentThread().getName()
                        + " Read balance: " + balance);

        // Simulate processing delay
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        // Validate
        if (balance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Debit
        account.setBalance(balance.subtract(amount));

        return "Debit successful";
    }

    public void testConcurrentDebit() {

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        CountDownLatch latch = new CountDownLatch(1);

        Runnable task1 = () -> {
            try {
                latch.await();

                System.out.println("Thread 1 started");

                System.out.println(
                        debitService.debit(
                                101L, new BigDecimal("7000")));

            } catch (Exception e) {
                System.out.println(
                        "Thread 1: " + e.getMessage());
            }
        };

        Runnable task2 = () -> {
            try {
                latch.await();

                System.out.println("Thread 2 started");

                System.out.println(
                        debitService.debit(
                                101L, new BigDecimal("6000")));

            } catch (Exception e) {
                System.out.println(
                        "Thread 2: " + e.getMessage());
            }
        };

        executor.submit(task1);
        executor.submit(task2);

        latch.countDown();

        executor.shutdown();
    }
}
