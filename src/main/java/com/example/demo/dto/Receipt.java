package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public record Receipt(List<ProductDiscountItem> items, BigDecimal total) {
    public record ProductDiscountItem(String productName, int quantity, BigDecimal price, BigDecimal originalPrice,
                                      BigDecimal discount) {
    }
}
