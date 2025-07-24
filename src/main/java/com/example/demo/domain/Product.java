package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String category;

    private BigDecimal price;

    private int stock;
}
