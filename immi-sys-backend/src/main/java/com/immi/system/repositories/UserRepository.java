package com.immi.system.repositories;

import com.immi.system.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    
// Custom query to find a user by their email (used for authentication)
    UserModel findByEmail(String email);

    public boolean existsByEmail(String email);
    
}
