package com.elearning.platform.repositories;

import com.elearning.platform.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * QuestionRepository - Repository pour Question
 * Chemin: src/main/java/com/elearning/platform/repositories/QuestionRepository.java
 * 
 * Méthodes: findByQuizId, etc.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    /**
     * Trouve les questions d'un quiz
     */
    List<Question> findByQuizId(Long quizId);
}