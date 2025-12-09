package com.elearning.platform.controller;

import com.elearning.platform.dto.QuizDto;
import com.elearning.platform.dto.QuizSubmitDto;
import com.elearning.platform.model.Quiz;
import com.elearning.platform.model.Question;
import com.elearning.platform.model.QuizAttempt;
import com.elearning.platform.model.User;
import com.elearning.platform.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<Quiz>> getQuizzesByLesson(@PathVariable Long lessonId) {
        List<Quiz> quizzes = quizService.findQuizzesByLesson(lessonId);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long quizId) {
        Optional<Quiz> quiz = quizService.findQuizById(quizId);
        if (quiz.isPresent()) {
            return ResponseEntity.ok(quiz.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuestionsForQuiz(@PathVariable Long quizId) {
        List<Question> questions = quizService.getQuestionsForQuiz(quizId);
        return ResponseEntity.ok(questions);
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody QuizDto quizDto) {
        Quiz quiz = quizService.createQuiz(quizDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(quiz);
    }

    @PutMapping("/{quizId}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long quizId, @Valid @RequestBody QuizDto quizDto) {
        Quiz updatedQuiz = quizService.updateQuiz(quizId, quizDto);
        return ResponseEntity.ok(updatedQuiz);
    }

    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{quizId}/submit")
    public ResponseEntity<?> submitQuiz(
            @PathVariable Long quizId,
            @Valid @RequestBody QuizSubmitDto submitDto,
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Utilisateur non authentifié"));
        }

        try {
            // Récupérer l'utilisateur depuis l'authentification
            // Note: adapter selon votre système d'authentification
            User user = (User) authentication.getPrincipal();

            QuizAttempt attempt = quizService.submitQuiz(quizId, user, submitDto);

            Map<String, Object> response = new HashMap<>();
            response.put("attemptId", attempt.getAttemptId());
            response.put("score", attempt.getScore());
            response.put("completedAt", attempt.getCompletedAt());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{quizId}/result")
    public ResponseEntity<?> getQuizResult(
            @PathVariable Long quizId,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Utilisateur non authentifié"));
        }

        try {
            User user = (User) authentication.getPrincipal();
            Optional<QuizAttempt> attempt = quizService.getAttemptForQuiz(quizId, user.getUserId());

            if (attempt.isPresent()) {
                return ResponseEntity.ok(attempt.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
