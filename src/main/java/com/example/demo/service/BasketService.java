package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.dto.Receipt;
import com.example.demo.repository.BasketRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.DealRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final DealRepository dealRepository;

    public void addItem(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow();

        Product product = productRepository.findById(productId)
                .orElseThrow();

        // If stock is insufficient, the operation should fail gracefully.
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product ID " + productId);
        }

        // When a customer adds a product to the basket, decrement stock accordingly.
        product.setStock(product.getStock() - quantity);
        Basket basket = customer.getBasket();
        if (basket == null) {
            basket = new Basket();
            basket.setCustomer(customer);
            basket.setItems(new ArrayList<>());
            customer.setBasket(basket);
        }

        Basket finalBasket = basket;
        BasketItem item = finalBasket.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseGet(() -> {
                    BasketItem newItem = new BasketItem();
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    newItem.setBasket(finalBasket);
                    finalBasket.getItems().add(newItem);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + quantity);

        productRepository.save(product);
        basketRepository.save(basket);
    }

    public void removeItem(Long customerId, Long productId, int quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow();

        Product product = productRepository.findById(productId)
                .orElseThrow();

        Basket basket = customer.getBasket();
        BasketItem item = basket.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow();

        int newQty = item.getQuantity() - quantity;

        if (newQty <= 0) {
            basket.getItems().remove(item);
        } else {
            item.setQuantity(newQty);
        }

        product.setStock(product.getStock() + quantity);
        productRepository.save(product);
        basketRepository.save(basket);
    }

    public Receipt getReceipt(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow();

        Basket basket = customer.getBasket();
        List<Receipt.ProductDiscountItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        List<Deal> deals = dealRepository.findAll().stream()
                .filter(deal -> deal.getExpiration().isAfter(LocalDateTime.now()))
                .toList();

        for (BasketItem item : basket.getItems()) {
            Product product = item.getProduct();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal price = product.getPrice();
            BigDecimal originalTotalPrice = price.multiply(quantity);

            // Get the applicable deal for the product
            Deal deal = deals.stream()
                    .filter(d -> d.getProductIds().contains(product.getId()))
                    .findFirst()
                    .orElse(null);

            BigDecimal discount = BigDecimal.ZERO;
            if (deal != null) {
                // Add discount deals for products (Example: Buy 1 get 50% off the second)
                int discountItemsCount = quantity.intValue() / 2;
                BigDecimal discountPerItem = price.multiply(
                        BigDecimal.valueOf(deal.getDiscount())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                ); // price * discount %
                discount = discountPerItem.multiply(BigDecimal.valueOf(discountItemsCount));
            }

            items.add(new Receipt.ProductDiscountItem(
                    product.getName(),
                    item.getQuantity(),
                    price,
                    originalTotalPrice,
                    discount
            ));

            total = total.add(originalTotalPrice.subtract(discount));
        }

        return new Receipt(items, total);
    }
}
