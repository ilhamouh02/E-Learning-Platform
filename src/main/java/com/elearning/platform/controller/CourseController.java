package com.elearning.platform.controller;

import com.elearning.platform.dto.CourseDto;
import com.elearning.platform.model.Course;
import com.elearning.platform.model.User;
import com.elearning.platform.services.core.impl.CourseService;
import com.elearning.platform.services.core.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseController - API REST pour les cours
 * Chemin: src/main/java/com/elearning/platform/controller/CourseController.java
 * 
 * Routes:
 * GET /api/courses - Lister tous les cours
 * GET /api/courses/:id - Détails d'un cours
 * POST /api/courses - Créer un cours (TEACHER)
 * PUT /api/courses/:id - Modifier un cours (TEACHER)
 * DELETE /api/courses/:id - Supprimer un cours (TEACHER/ADMIN)
 * GET /api/courses/search - Rechercher les cours
 * 
 * RÉSOUT:
 * ✅ Lister les cours disponibles
 * ✅ Créer un cours
 * ✅ Modifier un cours
 * ✅ Supprimer un cours
 * ✅ Rechercher par catégorie/enseignant
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    /**
     * GET /api/courses - Lister tous les cours
     * 
     * Query params (optionnel):
     * - category=Programming
     * - teacherId=1
     * 
     * RÉSOUT: Afficher le catalogue de tous les cours
     */
    @GetMapping
    public ResponseEntity<?> getAllCourses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long teacherId) {
        try {
            List<Course> courses = courseService.search(category, teacherId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", courses.size());
            response.put("courses", courses);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * GET /api/courses/:id - Récupérer les détails d'un cours
     * 
     * RÉSOUT: Afficher les détails complets d'un cours
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.findById(id);

            if (course == null) {
                return createErrorResponse(HttpStatus.NOT_FOUND, "Cours non trouvé");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("course", course);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * POST /api/courses - Créer un nouveau cours (TEACHER ONLY)
     * 
     * Body: {
     *   "title": "Java Advanced",
     *   "description": "Apprenez Java...",
     *   "category": "Programming",
     *   "teacherId": 2
     * }
     * 
     * RÉSOUT: Un enseignant crée un nouveau cours
     */
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto) {
        try {
            // Vérifier que le teacher existe
            if (courseDto.getTeacherId() == null) {
                return createErrorResponse(HttpStatus.BAD_REQUEST, "teacherId obligatoire");
            }

            User teacher = userService.findById(courseDto.getTeacherId());
            if (teacher == null) {
                return createErrorResponse(HttpStatus.NOT_FOUND, "Enseignant non trouvé");
            }

            // Vérifier que c'est un TEACHER
            if (!teacher.getRole().equals(User.Role.TEACHER)) {
                return createErrorResponse(HttpStatus.FORBIDDEN, "Seul un TEACHER peut créer des cours");
            }

            Course course = courseService.save(courseDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cours créé avec succès");
            response.put("course", course);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * PUT /api/courses/:id - Modifier un cours (TEACHER ONLY)
     * 
     * Body: {
     *   "title": "Java Advanced",
     *   "description": "...",
     *   "category": "Programming",
     *   "teacherId": 2
     * }
     * 
     * RÉSOUT: Un enseignant modifie son cours
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseDto courseDto) {
        try {
            Course course = courseService.findById(id);

            if (course == null) {
                return createErrorResponse(HttpStatus.NOT_FOUND, "Cours non trouvé");
            }

            Course updated = courseService.update(courseDto, id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cours modifié avec succès");
            response.put("course", updated);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * DELETE /api/courses/:id - Supprimer un cours (TEACHER/ADMIN ONLY)
     * 
     * RÉSOUT: Un enseignant/admin supprime un cours
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            Course course = courseService.findById(id);

            if (course == null) {
                return createErrorResponse(HttpStatus.NOT_FOUND, "Cours non trouvé");
            }

            courseService.deleteById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cours supprimé avec succès");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * GET /api/courses/teacher/:teacherId - Lister les cours d'un enseignant
     * 
     * RÉSOUT: Afficher tous les cours créés par un enseignant
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getCoursesByTeacher(@PathVariable Long teacherId) {
        try {
            List<Course> courses = courseService.findByTeacherId(teacherId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", courses.size());
            response.put("courses", courses);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * GET /api/courses/category/:category - Lister les cours par catégorie
     * 
     * RÉSOUT: Filtrer les cours par catégorie
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getCoursesByCategory(@PathVariable String category) {
        try {
            List<Course> courses = courseService.findByCategory(category);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", courses.size());
            response.put("courses", courses);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Utilitaire pour créer une réponse d'erreur
     */
    private ResponseEntity<?> createErrorResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return ResponseEntity.status(status).body(response);
    }
}