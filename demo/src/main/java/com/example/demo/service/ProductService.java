package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    // 1. ESTRATEGIA DE CACHÉ DISTRIBUIDO (REDIS)
    public Product create(Product product) {
        return repository.save(product);
    }

    @Cacheable(value = "products", key = "#id")
    public Product getById(Long id) {
        System.out.println("🐌 [CACHE MISS] Consultando BD Real para el ID: " + id);
        return repository.findById(id).orElseThrow(() -> new RuntimeException("No encontrado"));
    }

    @CachePut(value = "products", key = "#id")
    public Product update(Long id, Product product) {
        product.setId(id);
        return repository.save(product);
    }

    @CacheEvict(value = "products", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // 2. CONTROL DE TRANSACCIONES (ACID)

    // Atomicidad (RollbackFor), Aislamiento (READ_COMMITTED) y Consistencia de Caché (CacheEvict)
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    @CacheEvict(value = "products", key = "#productId") // Borra el caché porque el stock va a cambiar
    public void processPurchase(Long productId, Integer quantity) throws Exception {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new Exception("Producto no encontrado"));

        if (product.getStock() < quantity) {
            // Falla y hace ROLLBACK automático (nada se guarda en BD)
            throw new Exception("Stock insuficiente. Transacción revertida.");
        }

        product.setStock(product.getStock() - quantity);
        repository.save(product);
    }

    // Ejecuta una operación masiva nativa y limpia TODO el caché para que no queden precios viejos
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void applyDiscount(Double discount) {
        repository.applyBulkDiscount(discount);
    }
}
