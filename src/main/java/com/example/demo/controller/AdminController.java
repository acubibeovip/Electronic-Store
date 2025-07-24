package com.example.demo.controller;

import com.example.demo.domain.Customer;
import com.example.demo.domain.Deal;
import com.example.demo.domain.Product;
import com.example.demo.dto.DealDto;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.DealService;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ProductService productService;
    private final CustomerRepository customerRepository;
    private final DealService dealService;

    @PostMapping("/products")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getAll(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getAllProducts(PageRequest.of(page, size)));
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerRepository.save(customer));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerRepository.findAll());
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deals")
    public ResponseEntity<Deal> createDeal(@RequestBody DealDto deal) {
        return ResponseEntity.ok(dealService.createDeal(deal));
    }
}
