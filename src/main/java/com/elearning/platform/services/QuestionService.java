package com.elearning.platform.services;

import com.elearning.platform.dto.QuestionDto;
import com.elearning.platform.model.Question;
import com.elearning.platform.model.Quiz;
import com.elearning.platform.repositories.QuestionRepository;
import com.elearning.platform.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * QuestionService - Gère les questions de quiz
 * Chemin: src/main/java/com/elearning/platform/services/QuestionService.java
 * 
 * RÉSOUT LES PROBLÈMES:
 * ✅ Créer des questions
 * ✅ Modifier des questions
 * ✅ Récupérer les questions par quiz
 * ✅ Calculer les points totaux
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    /**
     * Crée une nouvelle question
     * 
     * RÉSOUT: Un enseignant ajoute une question à un quiz
     */
    public Question createQuestion(QuestionDto questionDto) {
        // Vérifier que le quiz existe
        Optional<Quiz> quizOpt = quizRepository.findById(questionDto.getQuizId());
        if (!quizOpt.isPresent()) {
            throw new RuntimeException("Quiz introuvable avec l'ID: " + questionDto.getQuizId());
        }

        Quiz quiz = quizOpt.get();

        // Créer la question
        Question question = new Question(
            questionDto.getQuestionText(),
            questionDto.getOptions(),
            questionDto.getCorrectAnswer(),
            questionDto.getPoints() != null ? questionDto.getPoints() : 1,
            quiz
        );

        return questionRepository.save(question);
    }

    /**
     * Met à jour une question
     * 
     * RÉSOUT: Un enseignant modifie une question
     */
    public Question updateQuestion(Long id, QuestionDto questionDto) {
        Optional<Question> questionOpt = questionRepository.findById(id);
        if (!questionOpt.isPresent()) {
            throw new RuntimeException("Question introuvable avec l'ID: " + id);
        }

        Question question = questionOpt.get();
        question.setQuestionText(questionDto.getQuestionText());
        question.setOptions(questionDto.getOptions());
        question.setCorrectAnswer(questionDto.getCorrectAnswer());
        question.setPoints(questionDto.getPoints());

        return questionRepository.save(question);
    }

    /**
     * Récupère les questions d'un quiz
     * 
     * RÉSOUT: Afficher les questions d'un quiz
     */
    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    /**
     * Récupère une question par ID
     * 
     * RÉSOUT: Afficher une question spécifique
     */
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    /**
     * Supprime une question
     * 
     * RÉSOUT: Un enseignant supprime une question
     */
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    /**
     * Calcule le nombre total de points pour un quiz
     * 
     * RÉSOUT: Calculer le score maximum possible
     */
    public Integer calculateTotalPoints(Long quizId) {
        List<Question> questions = questionRepository.findByQuizId(quizId);
        return questions.stream()
            .mapToInt(q -> q.getPoints() != null ? q.getPoints() : 1)
            .sum();
    }

    /**
     * Compte le nombre de questions dans un quiz
     * 
     * RÉSOUT: Afficher le nombre de questions
     */
    public long countByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId).size();
    }
}