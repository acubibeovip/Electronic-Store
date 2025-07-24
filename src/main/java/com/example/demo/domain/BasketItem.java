package com.example.demo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class BasketItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Basket basket;

    @ManyToOne
    private Product product;

    private int quantity;
}
