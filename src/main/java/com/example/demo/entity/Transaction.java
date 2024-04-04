package com.example.demo.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private float amount;
    private String paymentMethod;
    private String status;

    // getters and setters
}
