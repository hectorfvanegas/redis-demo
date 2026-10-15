package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Invocación a una optimización nativa (Procedimiento/Función en BD)
    @Modifying
    @Query(value = "UPDATE product SET price = price - (price * :discount / 100)", nativeQuery = true)
    void applyBulkDiscount(@Param("discount") Double discount);
}