package com.microservices.transactionmanagement;


import com.microservices.transactionmanagement.entity.Product;
import com.microservices.transactionmanagement.repo.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TransactionManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionManagementApplication.class, args);

    }

    @Bean
    CommandLineRunner initializeDatabase(
            ProductRepository repository) {

        return args -> repository.save(
                new Product(1L, "Laptop", 10));
    }


}
