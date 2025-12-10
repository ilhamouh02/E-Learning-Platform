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

@Service
public class UserService implements GenericService<UserDto, User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User save(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        User.Role role = User.Role.STUDENT;
        if (userDto.getRole() != null) {
            try {
                role = User.Role.valueOf(userDto.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                role = User.Role.STUDENT;
            }
        }
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User update(UserDto userDto, Long id) {
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
                    // Garder le rôle actuel
                }
            }
            
            return userRepository.save(user);
        }
        return null;
    }
}