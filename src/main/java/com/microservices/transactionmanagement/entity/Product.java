package com.microservices.transactionmanagement.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    private Long id;

    private String name;

    private int stock;

    protected Product() {
    }

    public Product(Long id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public void reduceStock(int quantity) {
        if (stock < quantity) {
            throw new IllegalStateException(
                    "Insufficient stock"
            );
        }

        stock -= quantity;
    }
}
