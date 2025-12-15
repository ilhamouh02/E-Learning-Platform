package com.elearning.platform.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtService - Génère et valide les JWT tokens
 * Chemin: src/main/java/com/elearning/platform/auth/JwtService.java
 * 
 * ✅ CORRIGÉ: Méthodes compatibles avec JwtAuthFilter
 */
@Service
public class JwtService {

    @Value("${jwt.secret:mySecretKeyForELearningPlatformProjectSpringBootJwtAuthentication123456789012345}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationInMs;

    /**
     * Génère un JWT token à partir de l'email et du rôle
     */
    public String generateToken(String email, String role) {
        return createToken(email, role);
    }

    /**
     * Crée le JWT token
     */
    private String createToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Récupère la clé de signature
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Extrait le username (email) du token
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrait le rôle du token
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    /**
     * Extrait toutes les claims du token
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Vérifie si le token est expiré
     */
    public Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * ✅ CORRIGÉ: Valide le token et retourne true/false
     * Compatible avec JwtAuthFilter
     */
    public Boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Vérifie si le token est valide pour un email spécifique
     */
    public Boolean isTokenValid(String token, String email) {
        final String username = extractUsername(token);
        return (username.equals(email) && !isTokenExpired(token));
    }
}