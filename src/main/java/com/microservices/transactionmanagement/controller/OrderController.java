package com.microservices.transactionmanagement.controller;

import com.microservices.transactionmanagement.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{productId}")
    public String placeOrder(@PathVariable Long productId,
                             @RequestParam int quantity,
                             @RequestParam(defaultValue = "false") boolean simulateFailure) {
        return orderService.placeOrder(productId, quantity, simulateFailure);
    }
}
