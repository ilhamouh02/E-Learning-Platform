package com.elearning.platform.services.core.impl;

import com.elearning.platform.dto.CourseDto;
import com.elearning.platform.model.Course;
import com.elearning.platform.model.User;
import com.elearning.platform.repositories.CourseRepository;
import com.elearning.platform.repositories.UserRepository;
import com.elearning.platform.services.core.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * CourseService - Gère les cours
 * Chemin: src/main/java/com/elearning/platform/services/core/impl/CourseService.java
 * 
 * RÉSOUT LES PROBLÈMES:
 * ✅ Créer des cours
 * ✅ Modifier des cours
 * ✅ Récupérer les cours par catégorie
 * ✅ Récupérer les cours par enseignant
 * ✅ Recherche avancée
 */
@Service
public class CourseService implements GenericService<CourseDto, Course> {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Crée un nouveau cours
     * 
     * RÉSOUT: Un enseignant crée un cours
     */
    @Override
    public Course save(CourseDto courseDto) {
        // Récupérer l'enseignant
        User teacher = null;
        if (courseDto.getTeacherId() != null) {
            Optional<User> teacherOpt = userRepository.findById(courseDto.getTeacherId());
            if (teacherOpt.isPresent()) {
                teacher = teacherOpt.get();
            }
        }

        // Créer le cours
        Course course = new Course(
            courseDto.getTitle(),
            courseDto.getDescription(),
            courseDto.getCategory(),
            teacher
        );

        return courseRepository.save(course);
    }

    /**
     * Récupère tous les cours
     * 
     * RÉSOUT: Afficher le catalogue de tous les cours
     */
    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    /**
     * Trouve un cours par ID
     * 
     * RÉSOUT: Afficher les détails d'un cours
     */
    @Override
    public Course findById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.orElse(null);
    }

    /**
     * Supprime un cours par ID
     * 
     * RÉSOUT: Un enseignant/admin supprime un cours
     */
    @Override
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    /**
     * Met à jour un cours
     * 
     * RÉSOUT: Un enseignant modifie son cours
     */
    @Override
    public Course update(CourseDto courseDto, Long id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        
        if (!courseOpt.isPresent()) {
            return null;
        }

        Course course = courseOpt.get();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setCategory(courseDto.getCategory());

        // Mettre à jour l'enseignant si fourni
        if (courseDto.getTeacherId() != null) {
            Optional<User> teacherOpt = userRepository.findById(courseDto.getTeacherId());
            teacherOpt.ifPresent(course::setTeacher);
        }

        return courseRepository.save(course);
    }

    /**
     * Trouve les cours créés par un enseignant
     * 
     * RÉSOUT: Afficher les cours d'un professeur
     */
    public List<Course> findByTeacherId(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    /**
     * Trouve les cours par catégorie
     * 
     * RÉSOUT: Filtrer les cours par catégorie (Développement, Frontend, etc.)
     */
    public List<Course> findByCategory(String category) {
        if (category == null || category.isEmpty()) {
            return courseRepository.findAll();
        }
        return courseRepository.findByCategory(category);
    }

    /**
     * Recherche avancée : par catégorie ET enseignant
     * 
     * RÉSOUT: Filtrer les cours par catégorie ET enseignant
     */
    public List<Course> search(String category, Long teacherId) {
        if (category != null && teacherId != null) {
            return courseRepository.findByCategoryAndTeacherId(category, teacherId);
        } else if (category != null) {
            return courseRepository.findByCategory(category);
        } else if (teacherId != null) {
            return courseRepository.findByTeacherId(teacherId);
        } else {
            return courseRepository.findAll();
        }
    }

    /**
     * Compte les cours totaux
     * 
     * RÉSOUT: Statistiques
     */
    public long countCourses() {
        return courseRepository.count();
    }

    /**
     * Compte les cours d'un enseignant
     * 
     * RÉSOUT: Savoir combien de cours crée un professeur
     */
    public long countCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId).size();
    }

    /**
     * Compte les cours par catégorie
     * 
     * RÉSOUT: Statistiques par catégorie
     */
    public long countByCategory(String category) {
        return courseRepository.findByCategory(category).size();
    }
}