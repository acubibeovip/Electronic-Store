package com.example.demo.service;

import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    private ProductService productService;
    private final Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    void getAllProducts() {
        productService.getAllProducts(pageable);
        verify(productRepository).findAll(pageable);
    }

    @Test
    void findProductByFilter() {
        productService.findProducts("category", BigDecimal.ZERO, BigDecimal.valueOf(50), pageable);
        verify(productRepository).findByCategoryAndPriceBetweenAndAvailable("category", BigDecimal.ZERO, BigDecimal.valueOf(50), pageable);
    }
}
