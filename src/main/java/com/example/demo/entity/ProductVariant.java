package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Product_Variants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long variantId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String variantName;
    private int quantity;
    private String color;
    private String size;

    // getters and setters
}
