package com.microservices.transactionmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data
public class Account {

    @Id
    private Long id;

    private String accountHolder;

    private BigDecimal balance;

    // Getters and setters
}