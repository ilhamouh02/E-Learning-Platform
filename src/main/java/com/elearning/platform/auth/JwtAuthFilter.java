package com.elearning.platform.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtAuthFilter - Filtre pour valider les JWT tokens
 * Chemin: src/main/java/com/elearning/platform/auth/JwtAuthFilter.java
 * 
 * ✅ CORRIGÉ: Utilise validateToken(String) correctement
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // Récupérer le token du header Authorization
            String token = extractTokenFromRequest(request);

            if (token != null && jwtService.validateToken(token)) {
                // Token valide : extraire l'email et charger l'utilisateur
                String email = jwtService.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Créer l'authentification
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Mettre en contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("JWT validation failed: " + e.getMessage());
        }

        // Continuer la chaîne de filtres
        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le token du header Authorization
     * Format: Bearer <token>
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}