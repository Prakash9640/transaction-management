package com.microservices.transactionmanagement.service;

import com.microservices.transactionmanagement.entity.IdempotencyRecord;
import com.microservices.transactionmanagement.entity.Payment;
import com.microservices.transactionmanagement.repo.IdempotencyRepository;
import com.microservices.transactionmanagement.repo.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final IdempotencyRepository idempotencyRepository;

    public PaymentService(PaymentRepository paymentRepository, IdempotencyRepository idempotencyRepository) {
        this.paymentRepository = paymentRepository;
        this.idempotencyRepository = idempotencyRepository;
    }



    public String makePayment(String idempotencyKey, Payment payment){

        Optional<IdempotencyRecord> record = idempotencyRepository.findByIdempotencyKey(idempotencyKey);
        // 1. Check whether the key already exists
        if(record.isPresent()){
            IdempotencyRecord idempotencyRecord = record.get();
            if("SUCCESS".equalsIgnoreCase(idempotencyRecord.getStatus())){
              return idempotencyRecord.getResponseBody();
            }
            throw new IllegalStateException("Payment is : "+record.get().getStatus()+" for this idempotency key: " + idempotencyKey + ". Please retry with different key....");
        }

        // 2. Create idempotency record
        IdempotencyRecord newRecord = new IdempotencyRecord();
        newRecord.setIdempotencyKey(idempotencyKey);
        newRecord.setStatus("IN_PROGRESS");
        idempotencyRepository.saveAndFlush(newRecord);


        // 3. Perform payment operation
        payment.setStatus("SUCCESS");
        Payment savedPayment = paymentRepository.save(payment);

        // 4. Update idempotency record
        newRecord.setStatus("SUCCESS");
         newRecord.setResponseBody("Payment successful for idempotency key: " + idempotencyKey);
        idempotencyRepository.save(newRecord);
        return newRecord.getResponseBody();
    }
    public String makePayment(int count, Payment payment){


        // 3. Perform payment operation
        payment.setStatus("SUCCESS");
        Payment savedPayment = paymentRepository.save(payment);

        // 4. Update idempotency record
//        newRecord.setStatus("SUCCESS");
//        newRecord.setResponseBody("Payment successful for idempotency key: " + idempotencyKey);
//        idempotencyRepository.save(newRecord);
//        return newRecord.getResponseBody();
        return "Payment successful for idempotency key: " + count;
    }
}
