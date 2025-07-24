package com.example.demo.controller;

import com.example.demo.domain.Product;
import com.example.demo.dto.BasketItemDto;
import com.example.demo.dto.Receipt;
import com.example.demo.service.BasketService;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final ProductService productService;
    private final BasketService basketService;

    @GetMapping("products")
    public ResponseEntity<Page<Product>> getFilteredProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable
    ) {
        Page<Product> products = productService.findProducts(category, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/{customerId}/items")
    public ResponseEntity<?> addItemToBasket(@PathVariable Long customerId,
                                             @RequestBody BasketItemDto request) {
        basketService.addItem(customerId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{customerId}/items")
    public ResponseEntity<?> removeItemFromBasket(@PathVariable Long customerId,
                                                  @RequestBody BasketItemDto request) {
        basketService.removeItem(customerId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/receipt/{customerId}")
    public ResponseEntity<Receipt> getReceipt(@PathVariable Long customerId) {
        Receipt receipt = basketService.getReceipt(customerId);
        return ResponseEntity.ok(receipt);
    }
}
