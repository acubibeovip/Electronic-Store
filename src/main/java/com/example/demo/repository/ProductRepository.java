package com.example.demo.repository;

import com.example.demo.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Products can be filtered by category, price range, or availability.
    // TODO: Not sure what is availability here
    @Query("""
           SELECT p FROM Product p
           WHERE (:category IS NULL OR p.category = :category)
             AND (:minPrice IS NULL OR p.price >= :minPrice)
             AND (:maxPrice IS NULL OR p.price <= :maxPrice)
           """)
    Page<Product> findByCategoryAndPriceBetweenAndAvailable(
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
}
