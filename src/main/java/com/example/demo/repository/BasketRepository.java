package com.example.demo.repository;

import com.example.demo.domain.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}
