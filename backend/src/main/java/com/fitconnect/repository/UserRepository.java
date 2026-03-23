package com.fitconnect.repository;

import com.fitconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * UserRepository - JPA Data Access Layer for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login)
    Optional<User> findByEmail(String email);

    // Check if email already exists (used for registration)
    Boolean existsByEmail(String email);

    // Find all trainers or users by role
    java.util.List<User> findByRole(User.Role role);
}
