package com.microservices.transactionmanagement.controller;

import com.microservices.transactionmanagement.dto.TransferRequest;
import com.microservices.transactionmanagement.entity.Payment;
import com.microservices.transactionmanagement.service.OrderService;
import com.microservices.transactionmanagement.service.PaymentRetryService;
import com.microservices.transactionmanagement.service.PaymentService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private  final PaymentService paymentService;
    private  final PaymentRetryService paymentRetryService;

    public OrderController(OrderService orderService, PaymentService paymentService, PaymentRetryService paymentRetryService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.paymentRetryService = paymentRetryService;
    }

    @PostMapping("/{productId}")
    public String placeOrder(@PathVariable Long productId,
                             @RequestParam int quantity,
                             @RequestParam(defaultValue = "false") boolean simulateFailure) {
        return orderService.placeOrder(productId, quantity, simulateFailure);
    }

    @PostMapping("/payment")
    public String makePayment(@RequestBody Payment payment,
                              @RequestParam String idempotencyKey) {

        // Generate once before retrying
        String key = StringUtils.hasText(idempotencyKey) ? idempotencyKey : UUID.randomUUID().toString();


        // Every retry uses the same key
        try{
        return paymentService.makePayment(key, payment);
        }catch (Exception e){
            return "Payment failed: " + e.getMessage();
        }
    }
    @PostMapping("/transfer")
    public String transferPayment(@RequestBody TransferRequest transferRequest,
                                  @RequestParam String idempotencyKey) {

        // Generate once before retrying
        String key = StringUtils.hasText(idempotencyKey) ? idempotencyKey : UUID.randomUUID().toString();


        // Every retry uses the same key
        try{
            return paymentRetryService.processPayment(key, transferRequest);
        }catch (Exception e){
            return "Payment failed: " + e.getMessage();
        }
    }
}
