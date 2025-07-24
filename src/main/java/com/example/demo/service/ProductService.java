package com.example.demo.service;

import com.example.demo.domain.Product;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> findProducts(String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByCategoryAndPriceBetweenAndAvailable(category, minPrice, maxPrice, pageable);
    }
}
