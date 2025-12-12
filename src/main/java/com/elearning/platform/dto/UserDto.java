package com.elearning.platform.dto;

import com.elearning.platform.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    private String role; // "STUDENT", "TEACHER", "ADMIN"
    private String name;
    private String surname;
    private String username;
    private String imgUrl;

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : "STUDENT")
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .imgUrl(user.getImgUrl())
                .build();
    }
}
