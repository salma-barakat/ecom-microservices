package com.ecommerce.product.repo;

import com.ecommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepo extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p WHERE p.available = true AND p.quantity > 0 AND LOWER(p.name) " +
            "LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    List<Product> searchProducts(@Param("keyword") String keyword);

    Product getProductById(long id);
}
