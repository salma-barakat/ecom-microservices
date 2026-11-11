package com.ecommerce.user.repo;

import com.ecommerce.user.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends MongoRepository<Users, UUID> {
    Optional<Users> findByEmail(String email);
}
