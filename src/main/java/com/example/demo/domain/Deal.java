package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Deal {
    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    @CollectionTable(name = "deal_products", joinColumns = @JoinColumn(name = "deal_id"))
    @Column(name = "product_id")
    private List<Long> productIds = new ArrayList<>();

    private double discount;

    private LocalDateTime expiration;
}
