package com.digital.electronics.repo;

import com.digital.electronics.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByProductCode(String productCode);
    List<Product> findByProductName(String productName);
    List<Product> findAll();
}
