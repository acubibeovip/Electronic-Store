package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DealDto {
    private List<Long> productIds;
    private double discount;
    private LocalDateTime expiration;
}
