package com.digital.electronics.repo;

import com.digital.electronics.model.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {
    List<ShoppingCart> findByUserName(String userName);
}
