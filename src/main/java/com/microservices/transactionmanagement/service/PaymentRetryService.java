package com.microservices.transactionmanagement.service;


import com.microservices.transactionmanagement.dto.TransferRequest;
import com.microservices.transactionmanagement.entity.Payment;
import com.microservices.transactionmanagement.repo.IdempotencyRepository;
import com.microservices.transactionmanagement.repo.PaymentRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;



@Service
public class PaymentRetryService {

    private final PaymentRepository paymentRepository;

    private final IdempotencyRepository idempotencyRepository;

    private final PaymentService paymentService;

    private final TransferService transferService;

    private static int count=1;


    public PaymentRetryService(PaymentRepository paymentRepository, IdempotencyRepository idempotencyRepository, PaymentService paymentService, TransferService transferService) {
        this.paymentRepository = paymentRepository;
        this.idempotencyRepository = idempotencyRepository;
        this.paymentService = paymentService;
        this.transferService = transferService;
    }



   @Retry(name = "paymentService", fallbackMethod = "paymentFallback")
    public String processPayment(
            String idempotencyKey,
            TransferRequest transferRequest) {

       String result = transferService.transferMoney(transferRequest.getFromAccountId(), transferRequest.getToAccountId(), transferRequest.getAmount(), count++);
           return result;
   }

    public String paymentFallback(Exception e) {
        return "Fallback executed: " + e.getMessage();
    }

}
