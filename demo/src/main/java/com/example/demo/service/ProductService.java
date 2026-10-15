package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    // Crea y guarda en BD Real
    public Product create(Product product) {
        return repository.save(product);
    }

    // @Cacheable: Si está en Redis, no entra a este método. Si no está, entra, consulta BD y guarda en Redis.
    @Cacheable(value = "products", key = "#id")
    public Product getById(Long id) {
        System.out.println(" [CACHE MISS] Consultando BD Real para el ID: " + id);
        return repository.findById(id).orElseThrow(() -> new RuntimeException("No encontrado"));
    }

    // @CachePut: Actualiza la BD Real Y el valor guardado en Redis al mismo tiempo
    @CachePut(value = "products", key = "#id")
    public Product update(Long id, Product product) {
        product.setId(id);
        return repository.save(product);
    }

    // @CacheEvict: Borra de la BD Real y limpia el registro en Redis
    @CacheEvict(value = "products", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }
}