package com.elearning.platform.services;

import com.elearning.platform.dto.QuizDto;
import com.elearning.platform.dto.QuizSubmitDto;
import com.elearning.platform.model.*;
import com.elearning.platform.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * QuizService - Gère les quizzes/évaluations
 * Chemin: src/main/java/com/elearning/platform/services/QuizService.java
 * 
 * RÉSOUT LES PROBLÈMES:
 * ✅ Créer des quizzes
 * ✅ Modifier des quizzes
 * ✅ Soumettre des réponses
 * ✅ Calculer les scores
 * ✅ Vérifier si passé/échoué
 */
@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    /**
     * Crée un nouveau quiz
     * 
     * RÉSOUT: Un enseignant crée une évaluation
     */
    public Quiz createQuiz(QuizDto quizDto) {
        Optional<Lesson> lessonOpt = lessonRepository.findById(quizDto.getLessonId());
        if (!lessonOpt.isPresent()) {
            throw new RuntimeException("Leçon introuvable avec l'ID: " + quizDto.getLessonId());
        }

        Lesson lesson = lessonOpt.get();

        Quiz quiz = new Quiz(
            quizDto.getTitle(),
            quizDto.getTimeLimit(),
            quizDto.getPassingScore(),
            lesson
        );

        return quizRepository.save(quiz);
    }

    /**
     * Met à jour un quiz
     * 
     * RÉSOUT: Un enseignant modifie un quiz
     */
    public Quiz updateQuiz(Long id, QuizDto quizDto) {
        Optional<Quiz> quizOpt = quizRepository.findById(id);
        if (!quizOpt.isPresent()) {
            throw new RuntimeException("Quiz introuvable avec l'ID: " + id);
        }

        Quiz quiz = quizOpt.get();
        quiz.setTitle(quizDto.getTitle());
        quiz.setTimeLimit(quizDto.getTimeLimit());
        quiz.setPassingScore(quizDto.getPassingScore());

        return quizRepository.save(quiz);
    }

    /**
     * Récupère les quizzes d'une leçon
     * 
     * RÉSOUT: Afficher les évaluations d'une leçon
     */
    public List<Quiz> getQuizzesByLessonId(Long lessonId) {
        return quizRepository.findByLessonId(lessonId);
    }

    /**
     * Récupère un quiz par ID
     * 
     * RÉSOUT: Afficher les détails d'un quiz
     */
    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    /**
     * Supprime un quiz
     * 
     * RÉSOUT: Un enseignant supprime un quiz
     */
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    /**
     * Soumet les réponses d'un étudiant
     * 
     * RÉSOUT: Étudiant soumet le quiz et reçoit un score
     */
    public Map<String, Object> submitQuiz(QuizSubmitDto submitDto, User user) {
        Optional<Quiz> quizOpt = quizRepository.findById(submitDto.getQuizId());
        if (!quizOpt.isPresent()) {
            throw new RuntimeException("Quiz introuvable");
        }

        Quiz quiz = quizOpt.get();
        List<Question> questions = questionRepository.findByQuizId(quiz.getId());

        // Calculer le score
        int totalScore = 0;
        int earnedScore = 0;
        int correctAnswers = 0;

        for (Question q : questions) {
            totalScore += (q.getPoints() != null ? q.getPoints() : 1);
            String userAnswer = submitDto.getAnswers().get(q.getId());
            
            if (userAnswer != null && userAnswer.equals(q.getCorrectAnswer())) {
                earnedScore += (q.getPoints() != null ? q.getPoints() : 1);
                correctAnswers++;
            }
        }

        // Calculer le pourcentage
        int percentage = totalScore > 0 ? (earnedScore * 100) / totalScore : 0;

        // Sauvegarder la tentative
        QuizAttempt attempt = new QuizAttempt(quiz, user, percentage);
        quizAttemptRepository.save(attempt);

        // Retourner les résultats
        Map<String, Object> result = new HashMap<>();
        result.put("score", percentage);
        result.put("passed", percentage >= quiz.getPassingScore());
        result.put("passingScore", quiz.getPassingScore());
        result.put("totalQuestions", questions.size());
        result.put("correctAnswers", correctAnswers);
        result.put("earnedPoints", earnedScore);
        result.put("totalPoints", totalScore);

        return result;
    }

    /**
     * Récupère les résultats des quiz (info sur les questions)
     * 
     * RÉSOUT: Afficher les détails et corrections
     */
    public Map<String, Object> getQuizResults(Long quizId) {
        List<Question> questions = questionRepository.findByQuizId(quizId);

        int totalPoints = questions.stream()
            .mapToInt(q -> q.getPoints() != null ? q.getPoints() : 1)
            .sum();

        Map<String, Object> result = new HashMap<>();
        result.put("totalQuestions", questions.size());
        result.put("totalPoints", totalPoints);
        result.put("questions", questions);

        return result;
    }

    /**
     * Récupère la tentative précédente d'un étudiant
     * 
     * RÉSOUT: Afficher si l'étudiant a déjà fait ce quiz
     */
    public Optional<QuizAttempt> getPreviousAttempt(Long quizId, Long studentId) {
        return quizAttemptRepository.findByQuizIdAndStudentId(quizId, studentId);
    }

    /**
     * Récupère toutes les tentatives d'un étudiant
     * 
     * RÉSOUT: Afficher l'historique de l'étudiant
     */
    public List<QuizAttempt> getStudentAttempts(Long studentId) {
        return quizAttemptRepository.findByStudentId(studentId);
    }

    /**
     * Récupère toutes les tentatives d'un quiz
     * 
     * RÉSOUT: Voir les résultats de tous les étudiants
     */
    public List<QuizAttempt> getQuizAttempts(Long quizId) {
        return quizAttemptRepository.findByQuizId(quizId);
    }

    /**
     * Calcule la moyenne de classe pour un quiz
     * 
     * RÉSOUT: Statistiques pour l'enseignant
     */
    public Double getAverageScore(Long quizId) {
        List<QuizAttempt> attempts = getQuizAttempts(quizId);
        if (attempts.isEmpty()) {
            return 0.0;
        }
        return attempts.stream()
            .mapToDouble(QuizAttempt::getScore)
            .average()
            .orElse(0.0);
    }

    /**
     * Compte le nombre d'étudiants ayant passé
     * 
     * RÉSOUT: Statistiques de réussite
     */
    public long countPassed(Long quizId) {
        List<QuizAttempt> attempts = getQuizAttempts(quizId);
        Optional<Quiz> quiz = getQuizById(quizId);
        
        if (!quiz.isPresent()) {
            return 0;
        }

        return attempts.stream()
            .filter(a -> a.getScore() >= quiz.get().getPassingScore())
            .count();
    }

    /**
     * Compte le nombre d'étudiants ayant échoué
     * 
     * RÉSOUT: Statistiques d'échec
     */
    public long countFailed(Long quizId) {
        List<QuizAttempt> attempts = getQuizAttempts(quizId);
        Optional<Quiz> quiz = getQuizById(quizId);
        
        if (!quiz.isPresent()) {
            return 0;
        }

        return attempts.stream()
            .filter(a -> a.getScore() < quiz.get().getPassingScore())
            .count();
    }
}