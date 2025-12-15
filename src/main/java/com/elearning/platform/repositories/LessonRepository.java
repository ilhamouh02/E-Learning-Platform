package com.elearning.platform.repositories;

import com.elearning.platform.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LessonRepository - Repository pour Lesson
 * Chemin: src/main/java/com/elearning/platform/repositories/LessonRepository.java
 * 
 * Méthodes: findByCourseId, etc.
 */
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    
    /**
     * Trouve les leçons d'un cours
     */
    List<Lesson> findByCourseId(Long courseId);
}