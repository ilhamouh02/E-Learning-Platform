package com.elearning.platform.controller;

import com.elearning.platform.auth.JwtService;
import com.elearning.platform.dto.UserDto;
import com.elearning.platform.model.User;
import com.elearning.platform.services.core.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * TemplateController - Gère le rendu des pages HTML avec Thymeleaf
 * Chemin: src/main/java/com/elearning/platform/controller/TemplateController.java
 * 
 * ✅ CORRIGÉ : Ajout des handlers POST pour traiter les formulaires login/register
 * ✅ AVEC CHOIX DE RÔLE à l'inscription
 */
@Controller
public class TemplateController {

    // ========== INJECTIONS DE DÉPENDANCES ==========
    
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    // ========== ROUTES GET (Afficher les pages) ==========

    /**
     * GET / - Afficher la page d'accueil
     * Route: http://localhost:8080/
     * Retourne: index.html
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /**
     * GET /login - Afficher le formulaire de connexion
     * Route: http://localhost:8080/login
     * Retourne: login.html
     */
    @GetMapping("/login")
    public String login(Model model) {
        // Le modèle peut contenir des messages d'erreur/succès en cas de redirection
        return "login";
    }

    /**
     * GET /register - Afficher le formulaire d'inscription
     * Route: http://localhost:8080/register
     * Retourne: register.html
     */
    @GetMapping("/register")
    public String register(Model model) {
        // Ajouter un UserDto vide au modèle (optionnel, peut être utilisé pour validation côté serveur)
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    /**
     * GET /discover - Afficher la page de découverte des cours
     * Route: http://localhost:8080/discover
     * Retourne: discover.html
     * ✅ Accessible uniquement aux utilisateurs authentifiés (selon la configuration de sécurité)
     */
    @GetMapping("/discover")
    public String discover() {
        return "discover";
    }

    /**
     * GET /courses - Afficher la liste des cours
     * Route: http://localhost:8080/courses
     * Retourne: courses/courses.html
     */
    @GetMapping("/courses")
    public String courses() {
        return "courses/courses";
    }

    /**
     * GET /tutors - Afficher la liste des tuteurs/enseignants
     * Route: http://localhost:8080/tutors
     * Retourne: tutors/tutors.html
     */
    @GetMapping("/tutors")
    public String tutors() {
        return "tutors/tutors";
    }

    /**
     * GET /teachers - Alias pour /tutors
     * Route: http://localhost:8080/teachers
     * Retourne: tutors/tutors.html (même page que /tutors)
     */
    @GetMapping("/teachers")
    public String teachers() {
        return "tutors/tutors";
    }

    // ========== ROUTES POST (Traiter les formulaires) ==========

    /**
     * POST /register - Traiter le formulaire d'inscription
     * 
     * Processus:
     * 1. Valider les données reçues du formulaire
     * 2. Vérifier que l'email n'existe pas déjà
     * 3. Créer un nouvel utilisateur avec le rôle choisi
     * 4. Rediriger vers /login avec message de succès
     * 5. En cas d'erreur, réafficher le formulaire avec message d'erreur
     * 
     * Paramètres reçus du formulaire:
     * - email: L'email de l'utilisateur (OBLIGATOIRE, UNIQUE)
     * - password: Le mot de passe (OBLIGATOIRE, min 6 caractères)
     * - name: Le prénom (OBLIGATOIRE)
     * - surname: Le nom de famille (OBLIGATOIRE)
     * - username: Le pseudo (OPTIONNEL)
     * - imgUrl: L'URL de l'avatar (OPTIONNEL)
     * - role: Le rôle choisi (STUDENT, TEACHER, ADMIN) - défaut STUDENT
     */
    @PostMapping("/register")
    public String registerSubmit(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String imgUrl,
            @RequestParam(required = false, defaultValue = "STUDENT") String role, // ✅ RÔLE AVEC DÉFAUT
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // ✅ ÉTAPE 1 : Vérifier que l'email n'existe pas
            if (!userService.isEmailAvailable(email)) {
                // Email déjà utilisé → afficher erreur
                model.addAttribute("error", "❌ Cet email est déjà utilisé. Veuillez en choisir un autre.");
                model.addAttribute("email", email);
                model.addAttribute("name", name);
                model.addAttribute("surname", surname);
                model.addAttribute("username", username);
                model.addAttribute("role", role); // ✅ Conserver le rôle en cas d'erreur
                return "register"; // Réafficher le formulaire
            }

            // ✅ ÉTAPE 2 : Créer le DTO (Data Transfer Object)
            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setPassword(password); // Sera hashé par UserService
            userDto.setRole(role); // ✅ UTILISER LE RÔLE CHOISI PAR L'UTILISATEUR
            userDto.setName(name);
            userDto.setSurname(surname);
            userDto.setUsername(username != null ? username : ""); // Vide si non fourni
            userDto.setImgUrl(imgUrl != null ? imgUrl : ""); // Vide si non fourni

            // ✅ ÉTAPE 3 : Sauvegarder l'utilisateur en BDD
            User user = userService.save(userDto);

            // ✅ ÉTAPE 4 : Ajouter un message de succès
            redirectAttributes.addFlashAttribute("success",
                    "✅ Inscription réussie en tant que " + role + " ! 🎉 Veuillez vous connecter.");

            // ✅ ÉTAPE 5 : Rediriger vers login
            return "redirect:/login";

        } catch (RuntimeException e) {
            // Erreur métier (ex: email invalide, password trop court)
            model.addAttribute("error", "❌ " + e.getMessage());
            model.addAttribute("email", email);
            model.addAttribute("name", name);
            model.addAttribute("surname", surname);
            model.addAttribute("username", username);
            model.addAttribute("role", role); // ✅ Conserver le rôle
            return "register";

        } catch (Exception e) {
            // Erreur serveur
            model.addAttribute("error", "❌ Erreur serveur : " + e.getMessage());
            model.addAttribute("role", role); // ✅ Conserver le rôle
            return "register";
        }
    }

    /**
     * POST /login - Traiter le formulaire de connexion
     * 
     * Processus:
     * 1. Authentifier l'utilisateur avec Spring Security
     * 2. Générer un JWT token
     * 3. Rediriger vers /discover (page protégée)
     * 4. En cas d'erreur, réafficher le formulaire avec message d'erreur
     * 
     * Paramètres reçus du formulaire:
     * - username: L'email ou le pseudo (OBLIGATOIRE)
     * - password: Le mot de passe (OBLIGATOIRE)
     */
    @PostMapping("/login")
    public String loginSubmit(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // ✅ ÉTAPE 1 : Authentifier avec Spring Security
            // Spring Security va vérifier le password avec BCrypt et le comparer
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // ✅ ÉTAPE 2 : Récupérer l'utilisateur authentifié
            User user = userService.findByEmail(username);
            
            if (user != null) {
                // ✅ ÉTAPE 3 : Générer le JWT token
                // Le token contient : email, rôle, date d'émission, date d'expiration
                String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

                // ✅ ÉTAPE 4 : Ajouter les données à la session (via RedirectAttributes)
                redirectAttributes.addFlashAttribute("token", token);
                redirectAttributes.addFlashAttribute("userId", user.getId());
                redirectAttributes.addFlashAttribute("userEmail", user.getEmail());
                redirectAttributes.addFlashAttribute("userRole", user.getRole().name()); // ✅ Ajouter le rôle

                // ✅ ÉTAPE 5 : Rediriger vers /discover
                return "redirect:/discover";
            } else {
                // L'utilisateur n'existe pas (cas très rare car Spring Security aurait échoué avant)
                model.addAttribute("error", "❌ Utilisateur non trouvé");
                model.addAttribute("username", username);
                return "login";
            }

        } catch (AuthenticationException e) {
            // Les identifiants sont invalides (email/pseudo ou password incorrect)
            model.addAttribute("error", "❌ Email/Pseudo ou mot de passe incorrect");
            model.addAttribute("username", username);
            return "login"; // Réafficher le formulaire

        } catch (Exception e) {
            // Erreur serveur
            model.addAttribute("error", "❌ Erreur serveur : " + e.getMessage());
            model.addAttribute("username", username);
            return "login";
        }
    }
}