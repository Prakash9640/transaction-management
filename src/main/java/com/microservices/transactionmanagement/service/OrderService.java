package com.microservices.transactionmanagement.service;

import com.microservices.transactionmanagement.entity.CustomerOrder;
import com.microservices.transactionmanagement.entity.Product;
import com.microservices.transactionmanagement.repo.OrderRepository;
import com.microservices.transactionmanagement.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

//    @Transactional
    public String placeOrder(Long productId,
                             int quantity,
                             boolean simulateFailure) {
        Product product = productRepository.findById(productId).
                orElseThrow(() ->
                        new RuntimeException("Product not found"));

        product.reduceStock(quantity);
        orderRepository.save( new CustomerOrder(productId, quantity));
        if(simulateFailure){
            throw new RuntimeException("Simulated failure after order placement");
        }
        return "Order placed successfully";
    }
}
