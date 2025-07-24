package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Basket basket;
}
