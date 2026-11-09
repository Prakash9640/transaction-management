package com.microservices.transactionmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(
        name = "idempotency_records",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "idempotencyKey")
        }
)
@Data
public class IdempotencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    private String status;

    @Column(length = 2000)
    private String responseBody;

    // Getters and setters
}