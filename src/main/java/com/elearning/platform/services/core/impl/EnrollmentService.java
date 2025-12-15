package com.elearning.platform.services.core.impl;

import com.elearning.platform.dto.EnrollmentDto;
import com.elearning.platform.model.Course;
import com.elearning.platform.model.Enrollment;
import com.elearning.platform.model.User;
import com.elearning.platform.repositories.CourseRepository;
import com.elearning.platform.repositories.EnrollmentRepository;
import com.elearning.platform.repositories.UserRepository;
import com.elearning.platform.services.core.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * EnrollmentService - Gère les inscriptions étudiants
 * Chemin: src/main/java/com/elearning/platform/services/core/impl/EnrollmentService.java
 * 
 * RÉSOUT LES PROBLÈMES:
 * ✅ Inscrire un étudiant à un cours
 * ✅ Désinscrire un étudiant
 * ✅ Mettre à jour la progression
 * ✅ Trouver les inscriptions par étudiant
 * ✅ Trouver les inscriptions par cours
 */
@Service
public class EnrollmentService implements GenericService<EnrollmentDto, Enrollment> {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Crée une nouvelle inscription
     * 
     * RÉSOUT: Un étudiant s'inscrit à un cours
     */
    @Override
    public Enrollment save(EnrollmentDto enrollmentDto) {
        Optional<User> userOpt = userRepository.findById(enrollmentDto.getStudentId());
        Optional<Course> courseOpt = courseRepository.findById(enrollmentDto.getCourseId());

        if (!userOpt.isPresent() || !courseOpt.isPresent()) {
            throw new RuntimeException("Utilisateur ou cours introuvable");
        }

        User student = userOpt.get();
        Course course = courseOpt.get();

        // Créer l'inscription
        Enrollment enrollment = new Enrollment(student, course);
        return enrollmentRepository.save(enrollment);
    }

    /**
     * Récupère toutes les inscriptions
     * 
     * RÉSOUT: Afficher toutes les inscriptions (admin)
     */
    @Override
    public List<Enrollment> getAll() {
        return enrollmentRepository.findAll();
    }

    /**
     * Note: Cette méthode n'est pas vraiment utilisée pour Enrollment (clé composite)
     */
    @Override
    public Enrollment findById(Long id) {
        return null;
    }

    /**
     * Supprime une inscription par ID
     * 
     * RÉSOUT: Désinscrire un étudiant
     * Note: Utilisez deleteByStudentIdAndCourseId à la place
     */
    @Override
    public void deleteById(Long id) {
        // Non applicable pour Enrollment
    }

    /**
     * Met à jour une inscription
     * 
     * RÉSOUT: Mettre à jour la progression ou marquer comme complété
     */
    @Override
    public Enrollment update(EnrollmentDto enrollmentDto, Long id) {
        Optional<User> userOpt = userRepository.findById(enrollmentDto.getStudentId());
        Optional<Course> courseOpt = courseRepository.findById(enrollmentDto.getCourseId());

        if (!userOpt.isPresent() || !courseOpt.isPresent()) {
            return null;
        }

        User student = userOpt.get();
        Course course = courseOpt.get();

        // Créer/mettre à jour l'inscription
        Enrollment enrollment = new Enrollment(student, course);
        enrollment.setProgress(enrollmentDto.getProgress());
        enrollment.setCompleted(enrollmentDto.getCompleted());
        enrollment.setEnrolledAt(enrollmentDto.getEnrolledAt());

        return enrollmentRepository.save(enrollment);
    }

    /**
     * Trouve les inscriptions d'un étudiant
     * 
     * RÉSOUT: Afficher les cours auquel l'étudiant est inscrit
     */
    public List<Enrollment> findByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    /**
     * Trouve les inscriptions à un cours
     * 
     * RÉSOUT: Voir combien d'étudiants sont inscrits au cours
     */
    public List<Enrollment> findByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    /**
     * Supprime une inscription spécifique
     * 
     * RÉSOUT: Un étudiant se désinscrire d'un cours
     */
    public void deleteByStudentIdAndCourseId(Long studentId, Long courseId) {
        enrollmentRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }

    /**
     * Trouve une inscription spécifique
     * 
     * RÉSOUT: Vérifier si un étudiant est inscrit à un cours
     */
    public Enrollment findByStudentIdAndCourseId(Long studentId, Long courseId) {
        Optional<Enrollment> enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        return enrollment.orElse(null);
    }

    /**
     * Vérifie si un étudiant est inscrit à un cours
     * 
     * RÉSOUT: Vérification pour empêcher l'inscription double
     */
    public boolean isEnrolled(Long studentId, Long courseId) {
        return findByStudentIdAndCourseId(studentId, courseId) != null;
    }

    /**
     * Met à jour la progression d'un étudiant
     * 
     * RÉSOUT: Tracker la progression dans un cours
     */
    public Enrollment updateProgress(Long studentId, Long courseId, Integer progress) {
        Enrollment enrollment = findByStudentIdAndCourseId(studentId, courseId);
        if (enrollment != null) {
            enrollment.setProgress(progress);
            return enrollmentRepository.save(enrollment);
        }
        return null;
    }

    /**
     * Marque un cours comme complété
     * 
     * RÉSOUT: Marquer l'étudiant comme ayant terminé le cours
     */
    public Enrollment markAsCompleted(Long studentId, Long courseId) {
        Enrollment enrollment = findByStudentIdAndCourseId(studentId, courseId);
        if (enrollment != null) {
            enrollment.setCompleted(true);
            enrollment.setProgress(100);
            return enrollmentRepository.save(enrollment);
        }
        return null;
    }

    /**
     * Compte les inscriptions d'un étudiant
     * 
     * RÉSOUT: Savoir combien de cours l'étudiant suit
     */
    public Long countByStudentId(Long studentId) {
        return enrollmentRepository.countByStudentId(studentId);
    }

    /**
     * Compte les inscriptions à un cours
     * 
     * RÉSOUT: Savoir combien d'étudiants sont inscrits
     */
    public Long countByCourseId(Long courseId) {
        return enrollmentRepository.countByCourseId(courseId);
    }
}