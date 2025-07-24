package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Customer customer;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BasketItem> items = new ArrayList<>();
}
