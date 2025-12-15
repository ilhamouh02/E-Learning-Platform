package com.elearning.platform.repositories;

import com.elearning.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository - Repository pour User
 * Chemin: src/main/java/com/elearning/platform/repositories/UserRepository.java
 * 
 * Méthodes: findByEmail, findAll, save, delete
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Trouve un utilisateur par email
     */
    Optional<User> findByEmail(String email);
}