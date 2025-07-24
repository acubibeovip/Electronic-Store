package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.dto.Receipt;
import com.example.demo.repository.BasketRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.DealRepository;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class BasketServiceTest {
    @Mock private BasketRepository basketRepository;
    @Mock private ProductRepository productRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private DealRepository dealRepository;
    private BasketService basketService;
    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        basketService = new BasketService(basketRepository, productRepository, customerRepository, dealRepository);
    }

    private void initData() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Customer Name");
        customer.setEmail("test@example.com");

        product = new Product();
        product.setId(1L);
        product.setName("Product Name");
        product.setStock(10);
        product.setCategory("Category");
        product.setPrice(BigDecimal.valueOf(100));
    }

    @Test
    void testAddItemToBasket() {
        // Prepare
        initData();
        // When
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        // Call actual logic
        basketService.addItem(customer.getId(), product.getId(), 10);

        // Evaluate
        assertEquals(0, product.getStock()); // Since 10 - 10 = 0
        assertNotNull(customer.getBasket());
        assertEquals(1, customer.getBasket().getItems().size());

    }

    @Test
    void testRemoveItemFromBasket() {
        // Prepare
        initData();
        Basket basket = new Basket();
        basket.setCustomer(customer);

        BasketItem item = new BasketItem();
        item.setProduct(product);
        item.setQuantity(5);
        item.setBasket(basket);

        basket.setItems(new ArrayList<>(List.of(item)));
        customer.setBasket(basket);

        // When
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        when(basketRepository.save(any())).thenReturn(basket);

        // Call actual logic
        basketService.removeItem(customer.getId(), product.getId(), 2); // Remove 2 out of 5

        // Evaluate
        assertEquals(3, item.getQuantity());
        assertEquals(12, product.getStock()); // Original was 10, now +2
    }

    @Test
    void testGetReceipt() {
        // Prepare
        initData();
        Basket basket = new Basket();
        basket.setCustomer(customer);

        BasketItem item = new BasketItem();
        item.setProduct(product);
        item.setQuantity(4);
        item.setBasket(basket);

        basket.setItems(new ArrayList<>(List.of(item)));
        customer.setBasket(basket);

        Deal deal = new Deal();
        deal.setProductIds(List.of(product.getId()));
        deal.setDiscount(50); // 50%
        deal.setExpiration(LocalDateTime.now().plusDays(1));

        // When
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(dealRepository.findAll()).thenReturn(List.of(deal));

        // Call actual logic
        Receipt receipt = basketService.getReceipt(1L);

        // Evaluate
        assertNotNull(receipt);
        assertEquals(1, receipt.items().size());

        Receipt.ProductDiscountItem itemResult = receipt.items().get(0);

        assertEquals("Product Name", itemResult.productName());
        assertEquals(4, itemResult.quantity());
        assertEquals(BigDecimal.valueOf(100), itemResult.price());

        BigDecimal expectedOriginalPrice = BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(4)); // 400
        BigDecimal expectedDiscount = BigDecimal.valueOf(100)
                .multiply(BigDecimal.valueOf(0.5)) // 50% off
                .multiply(BigDecimal.valueOf(2));  // applies to 2 items out of 4

        BigDecimal expectedTotal = expectedOriginalPrice.subtract(expectedDiscount);

        assertEquals(expectedOriginalPrice, itemResult.originalPrice());
        assertEquals(expectedDiscount.setScale(2), itemResult.discount());
        assertEquals(expectedTotal.setScale(2), receipt.total());
    }
}
