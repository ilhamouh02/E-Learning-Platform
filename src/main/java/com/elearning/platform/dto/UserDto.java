package com.elearning.platform.dto;

import com.elearning.platform.model.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;
    
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;
    
    private String role; // "STUDENT", "TEACHER", "ADMIN"
    
    // Constructeurs
    public UserDto() {}
    
    public UserDto(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    public UserDto(User user) {
        this.email = user.getEmail();
        this.role = user.getRole() != null ? user.getRole().name() : "STUDENT";
    }
    
    // Getters et Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}