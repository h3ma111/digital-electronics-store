package com.digital.electronics.repo;

import java.util.List;

import com.digital.electronics.model.CartUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartUserRepository extends MongoRepository<CartUser, String> {

    CartUser findByFirstName(String firstName);
    CartUser findByUserName(String userName);
    CartUser findByEmail(String email);
    List<CartUser> findByLastName(String lastName);

}
