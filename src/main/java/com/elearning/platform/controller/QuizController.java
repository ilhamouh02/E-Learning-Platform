package com.elearning.platform.controller;

import com.elearning.platform.dto.QuizDto;
import com.elearning.platform.dto.QuizSubmitDto;
import com.elearning.platform.model.Quiz;
import com.elearning.platform.model.QuizAttempt;
import com.elearning.platform.model.User;
import com.elearning.platform.services.QuizService;
import com.elearning.platform.services.core.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * QuizController - API REST pour les quizzes
 * Chemin: src/main/java/com/elearning/platform/controller/QuizController.java
 * 
 * Routes:
 * GET /api/quizzes/lesson/:lessonId - Lister les quizzes d'une leçon
 * GET /api/quizzes/:id - Récupérer les détails d'un quiz
 * POST /api/quizzes - Créer un quiz (TEACHER)
 * POST /api/quizzes/:id/submit - Soumettre les réponses
 * GET /api/quizzes/:id/attempts/:studentId - Récupérer les tentatives
 * GET /api/quizzes/:id/results - Afficher les résultats
 * 
 * RÉSOUT:
 * ✅ Afficher les quizzes
 * ✅ Soumettre les réponses
 * ✅ Calculer les scores
 * ✅ Afficher les statistiques
 */
@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    /**
     * GET /api/quizzes/lesson/:lessonId - Lister les quizzes d'une leçon
     * 
     * RÉSOUT: Afficher les évaluations d'une leçon
     */
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<?> getQuizzesByLesson(@PathVariable Long lessonId) {
        try {
            List<Quiz> quizzes = quizService.getQuizzesByLessonId(lessonId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", quizzes.size());
            response.put("quizzes", quizzes);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * GET /api/quizzes/:id - Récupérer les détails d'un quiz
     * 
     * RÉSOUT: Afficher les informations du quiz
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable Long id) {
        try {
            Optional<Quiz> quizOpt = quizService.getQuizById(id);

            if (!quizOpt.isPresent()) {
                return createErrorResponse(HttpStatus.NOT_FOUND, "Quiz non trouvé");
            }

            Quiz quiz = quizOpt.get();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("quiz", quiz);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * POST /api/quizzes - Créer un quiz (TEACHER ONLY)
     * 
     * Body: {
     *   "title": "Quiz Java",
     *   "timeLimit": 30,
     *   "passingScore": 70,
     *   "lessonId": 1
     * }
     * 
     * RÉSOUT: Un enseignant crée une évaluation
     */
    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody QuizDto quizDto) {
        try {
            Quiz quiz = quizService.createQuiz(quizDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Quiz créé avec succès");
            response.put("quiz", quiz);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * POST /api/quizzes/:id/submit - Soumettre les réponses du quiz
     * 
     * Body: {
     *   "quizId": 1,
     *   "studentId": 3,
     *   "answers": {
     *     "1": "A",
     *     "2": "B",
     *     "3": "C"
     *   }
     * }
     * 
     * Response: {
     *   "score": 85,
     *   "passed": true,
     *   "correctAnswers": 8,
     *   "totalPoints": 100,
     *   ...
     * }
     * 
     * RÉSOUT: Un étudiant soumet le quiz et reçoit un score
     */
    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitQuiz(@PathVariable Long id, @RequestBody QuizSubmitDto submitDto) {
        try {
            // Récupérer l'utilisateur
            User user = userService.findById(submitDto.getStudentId());
            if (user == null) {
                return createErrorResponse(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
            }

            // Soumettre le quiz et calculer le score
            submitDto.setQuizId(id);
            Map<String, Object> result = quizService.submitQuiz(submitDto, user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Quiz soumis avec succès");
            response.putAll(result);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * GET /api/quizzes/:id/attempts/:studentId - Récupérer les tentatives d'un étudiant
     * 
     * RÉSOUT: Afficher l'historique des tentatives
     */
    @GetMapping("/{id}/attempts/{studentId}")
    public ResponseEntity<?> getAttempts(@PathVariable Long id, @PathVariable Long studentId) {
        try {
            Optional<QuizAttempt> attemptOpt = quizService.getPreviousAttempt(id, studentId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("attempted", attemptOpt.isPresent());
            if (attemptOpt.isPresent()) {
                response.put("attempt", attemptOpt.get());
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * GET /api/quizzes/:id/results - Afficher les résultats du quiz
     * 
     * RÉSOUT: Afficher les stats et corrections
     */
    @GetMapping("/{id}/results")
    public ResponseEntity<?> getResults(@PathVariable Long id) {
        try {
            Map<String, Object> results = quizService.getQuizResults(id);

            // Ajouter les statistiques
            Double avgScore = quizService.getAverageScore(id);
            Long passedCount = quizService.countPassed(id);
            Long failedCount = quizService.countFailed(id);

            results.put("averageScore", avgScore);
            results.put("passedStudents", passedCount);
            results.put("failedStudents", failedCount);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.putAll(results);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * GET /api/quizzes/:id/stats - Statistiques globales
     * 
     * RÉSOUT: Afficher les statistiques du quiz
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getStats(@PathVariable Long id) {
        try {
            Double avgScore = quizService.getAverageScore(id);
            Long passed = quizService.countPassed(id);
            Long failed = quizService.countFailed(id);
            Long total = passed + failed;

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("averageScore", String.format("%.2f%%", avgScore));
            response.put("passedStudents", passed);
            response.put("failedStudents", failed);
            response.put("totalAttempts", total);
            response.put("passPercentage", total > 0 ? String.format("%.2f%%", (passed * 100.0 / total)) : "0%");

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