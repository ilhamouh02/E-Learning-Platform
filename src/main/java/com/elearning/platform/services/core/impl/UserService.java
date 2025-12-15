package com.elearning.platform.services.core.impl;

import com.elearning.platform.dto.UserDto;
import com.elearning.platform.model.User;
import com.elearning.platform.repositories.UserRepository;
import com.elearning.platform.services.core.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * UserService - Gère les utilisateurs
 * Chemin: src/main/java/com/elearning/platform/services/core/impl/UserService.java
 * 
 * RÉSOUT LES PROBLÈMES:
 * ✅ Créer des utilisateurs avec hash de password
 * ✅ Trouver un utilisateur par email
 * ✅ Mettre à jour les informations utilisateur
 * ✅ Valider les données
 */
@Service
public class UserService implements GenericService<UserDto, User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Crée un nouvel utilisateur
     * 
     * RÉSOUT: Créer un compte utilisateur
     * - Hash le password avec BCrypt
     * - Assigne le rôle par défaut STUDENT
     */
    @Override
    public User save(UserDto userDto) {
        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé : " + userDto.getEmail());
        }

        // Créer l'utilisateur
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // Hash le password
        
        // Assigner le rôle
        User.Role role = User.Role.STUDENT;
        if (userDto.getRole() != null && !userDto.getRole().isEmpty()) {
            try {
                role = User.Role.valueOf(userDto.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                role = User.Role.STUDENT; // Par défaut
            }
        }
        user.setRole(role);

        return userRepository.save(user);
    }

    /**
     * Récupère tous les utilisateurs
     * 
     * RÉSOUT: Afficher la liste de tous les utilisateurs
     */
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Trouve un utilisateur par ID
     * 
     * RÉSOUT: Récupérer un utilisateur spécifique
     */
    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    /**
     * Supprime un utilisateur par ID
     * 
     * RÉSOUT: Supprimer un compte utilisateur
     */
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Met à jour un utilisateur
     * 
     * RÉSOUT: Modifier les informations utilisateur
     */
    @Override
    public User update(UserDto userDto, Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        
        if (!userOpt.isPresent()) {
            return null;
        }

        User user = userOpt.get();
        user.setEmail(userDto.getEmail());

        // Si le password est fourni, le hasher
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        // Mettre à jour le rôle si fourni
        if (userDto.getRole() != null && !userDto.getRole().isEmpty()) {
            try {
                user.setRole(User.Role.valueOf(userDto.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Garder le rôle actuel
            }
        }

        return userRepository.save(user);
    }

    /**
     * Trouve un utilisateur par email
     * 
     * RÉSOUT: Recherche lors du login
     */
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    /**
     * Vérifie si un email est disponible
     * 
     * RÉSOUT: Validation à l'inscription
     */
    public boolean isEmailAvailable(String email) {
        return !userRepository.findByEmail(email).isPresent();
    }

    /**
     * Compte le nombre total d'utilisateurs
     * 
     * RÉSOUT: Statistiques
     */
    public long countUsers() {
        return userRepository.count();
    }

    /**
     * Compte le nombre d'utilisateurs par rôle
     * 
     * RÉSOUT: Statistiques par rôle
     */
    public long countByRole(User.Role role) {
        return userRepository.findAll().stream()
            .filter(u -> u.getRole() == role)
            .count();
    }
}