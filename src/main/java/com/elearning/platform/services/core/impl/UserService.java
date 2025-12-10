package com.elearning.platform.services.core.impl;

import com.elearning.platform.dto.UserDto;
import com.elearning.platform.model.User;
import com.elearning.platform.repositories.UserRepository;
import com.elearning.platform.services.core.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements GenericService<UserDto> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto save(UserDto userDto) {
        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // Créer un nouvel utilisateur
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        // Définir le rôle (par défaut STUDENT)
        User.Role role = User.Role.STUDENT;
        if (userDto.getRole() != null) {
            try {
                role = User.Role.valueOf(userDto.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                role = User.Role.STUDENT;
            }
        }
        user.setRole(role);

        user = userRepository.save(user);
        return new UserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserDto::new).orElse(null);
    }

    public UserDto findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(UserDto::new).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail(userDto.getEmail());
            
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
            
            if (userDto.getRole() != null) {
                try {
                    user.setRole(User.Role.valueOf(userDto.getRole().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    // Garder le rôle actuel si invalide
                }
            }
            
            user = userRepository.save(user);
            return new UserDto(user);
        }
        return null;
    }
}