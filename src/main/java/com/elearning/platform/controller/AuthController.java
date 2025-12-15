package com.elearning.platform.controller;

import com.elearning.platform.auth.JwtService;
import com.elearning.platform.dto.UserDto;
import com.elearning.platform.model.User;
import com.elearning.platform.services.core.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthController - API REST pour l'authentification
 * Chemin: src/main/java/com/elearning/platform/controller/AuthController.java
 * 
 * Routes:
 * POST /api/auth/register - Créer un compte
 * POST /api/auth/login - Se connecter (retourne JWT)
 * POST /api/auth/logout - Se déconnecter
 * GET /api/auth/profile - Récupérer le profil
 * 
 * RÉSOUT:
 * ✅ Enregistrement utilisateur
 * ✅ Authentification avec JWT
 * ✅ Gestion de session
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    /**
     * POST /api/auth/register - Créer un nouveau compte
     * 
     * Body: {
     *   "email": "test@test.com",
     *   "password": "password123",
     *   "role": "STUDENT"
     * }
     * 
     * RÉSOUT: Enregistrement d'un nouvel utilisateur
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        try {
            // Vérifier que l'email n'existe pas
            if (!userService.isEmailAvailable(userDto.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Email déjà utilisé"));
            }

            // Créer l'utilisateur
            User user = userService.save(userDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Utilisateur créé avec succès");
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * POST /api/auth/login - Se connecter
     * 
     * Body: {
     *   "email": "test@test.com",
     *   "password": "password123"
     * }
     * 
     * Response: {
     *   "token": "eyJhbGciOiJIUzI1NiIs...",
     *   "userId": 1,
     *   "email": "test@test.com",
     *   "role": "STUDENT"
     * }
     * 
     * RÉSOUT: Authentification et génération JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        try {
            // Vérifier que l'utilisateur existe
            User user = userService.findByEmail(userDto.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Email ou mot de passe incorrect"));
            }

            // Authentifier avec Spring Security
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
            );

            // Générer le JWT
            String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("token", token);
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(createErrorResponse("Email ou mot de passe incorrect"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * GET /api/auth/profile - Récupérer le profil de l'utilisateur connecté
     * 
     * Header: Authorization: Bearer <token>
     * 
     * Response: {
     *   "id": 1,
     *   "email": "test@test.com",
     *   "role": "STUDENT"
     * }
     * 
     * RÉSOUT: Récupérer les infos de l'utilisateur connecté
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extraire le token
            String token = authHeader.replace("Bearer ", "");
            
            // Extraire l'email du token
            String email = jwtService.extractUsername(token);
            
            // Récupérer l'utilisateur
            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Utilisateur non trouvé"));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(createErrorResponse("Token invalide ou expiré"));
        }
    }

    /**
     * POST /api/auth/logout - Se déconnecter
     * 
     * Header: Authorization: Bearer <token>
     * 
     * RÉSOUT: Fermer la session
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Déconnecté avec succès");
        return ResponseEntity.ok(response);
    }

    /**
     * Utilitaire pour créer une réponse d'erreur
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return response;
    }
}