package com.elearning.platform.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * UserDto - Data Transfer Object pour User
 * Chemin: src/main/java/com/elearning/platform/dto/UserDto.java
 *
 * Utilisé pour les requêtes HTTP de login/register
 */
public class UserDto {

    private String name;
    private String surname;
    private String username;
    private String imgUrl;

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

    // Getters et Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}