package com.elearning.platform.repositories;

import com.elearning.platform.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * QuizAttemptRepository - Repository pour QuizAttempt
 * Chemin: src/main/java/com/elearning/platform/repositories/QuizAttemptRepository.java
 * 
 * Méthodes: findByStudentId, findByQuizId, findByQuizIdAndStudentId
 */
@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    
    /**
     * Trouve une tentative de quiz par étudiant
     */
    Optional<QuizAttempt> findByQuizIdAndStudentId(Long quizId, Long studentId);
    
    /**
     * Trouve les tentatives d'un étudiant
     */
    List<QuizAttempt> findByStudentId(Long studentId);
    
    /**
     * Trouve les tentatives d'un quiz
     */
    List<QuizAttempt> findByQuizId(Long quizId);
}