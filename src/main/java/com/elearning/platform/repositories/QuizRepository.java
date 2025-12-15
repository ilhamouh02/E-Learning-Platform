package com.elearning.platform.repositories;

import com.elearning.platform.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * QuizRepository - Repository pour Quiz
 * Chemin: src/main/java/com/elearning/platform/repositories/QuizRepository.java
 * 
 * Méthodes: findByLessonId, etc.
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    /**
     * Trouve les quizzes d'une leçon
     */
    List<Quiz> findByLessonId(Long lessonId);
}