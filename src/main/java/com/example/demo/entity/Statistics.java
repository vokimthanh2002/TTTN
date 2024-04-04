package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statisticsId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate month;
    private BigDecimal revenue;
    private BigDecimal profit;
    private int totalOrders;
    private int totalProducts;

    // getters and setters
}
