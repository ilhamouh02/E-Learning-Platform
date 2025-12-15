package com.elearning.platform.repositories;

import com.elearning.platform.model.Enrollment;
import com.elearning.platform.model.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * EnrollmentRepository - Repository pour Enrollment
 * Chemin: src/main/java/com/elearning/platform/repositories/EnrollmentRepository.java
 * 
 * ✅ CORRIGÉ: Utilise EnrollmentId comme type de clé (pas Long)
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
    
    /**
     * Trouve les inscriptions d'un étudiant par student_id
     */
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId")
    List<Enrollment> findByStudentId(@Param("studentId") Long studentId);
    
    /**
     * Trouve les inscriptions à un cours par course_id
     */
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId")
    List<Enrollment> findByCourseId(@Param("courseId") Long courseId);
    
    /**
     * Trouve une inscription spécifique par student_id ET course_id
     */
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId")
    Optional<Enrollment> findByStudentIdAndCourseId(
        @Param("studentId") Long studentId,
        @Param("courseId") Long courseId
    );
    
    /**
     * Supprime une inscription par student_id ET course_id
     */
    @Query("DELETE FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId")
    void deleteByStudentIdAndCourseId(
        @Param("studentId") Long studentId,
        @Param("courseId") Long courseId
    );
    
    /**
     * Compte les inscriptions d'un étudiant
     */
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId")
    Long countByStudentId(@Param("studentId") Long studentId);
    
    /**
     * Compte les inscriptions à un cours
     */
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId")
    Long countByCourseId(@Param("courseId") Long courseId);
}