package com.elearning.platform.repositories;

import com.elearning.platform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CourseRepository - Repository pour Course
 * Chemin: src/main/java/com/elearning/platform/repositories/CourseRepository.java
 * 
 * Méthodes: findByTeacherId, findByCategory, search, etc.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    /**
     * Trouve les cours créés par un enseignant
     */
    List<Course> findByTeacherId(Long teacherId);
    
    /**
     * Trouve les cours par catégorie
     */
    List<Course> findByCategory(String category);
    
    /**
     * Trouve les cours par catégorie ET enseignant
     */
    List<Course> findByCategoryAndTeacherId(String category, Long teacherId);
}