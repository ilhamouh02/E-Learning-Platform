package com.elearning.platform.services;

import com.elearning.platform.dto.LessonDto;
import com.elearning.platform.model.Course;
import com.elearning.platform.model.Lesson;
import com.elearning.platform.repositories.CourseRepository;
import com.elearning.platform.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * LessonService - Gère les leçons
 * Chemin: src/main/java/com/elearning/platform/services/LessonService.java
 * 
 * RÉSOUT LES PROBLÈMES:
 * ✅ Créer des leçons
 * ✅ Modifier des leçons
 * ✅ Récupérer les leçons par cours
 * ✅ Supprimer des leçons
 */
@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Crée une nouvelle leçon
     * 
     * RÉSOUT: Un enseignant ajoute une leçon à son cours
     */
    public Lesson createLesson(LessonDto lessonDto) {
        // Vérifier que le cours existe
        Optional<Course> courseOpt = courseRepository.findById(lessonDto.getCourseId());
        if (!courseOpt.isPresent()) {
            throw new RuntimeException("Cours introuvable avec l'ID: " + lessonDto.getCourseId());
        }

        Course course = courseOpt.get();

        // Créer la leçon
        Lesson lesson = new Lesson(
            lessonDto.getTitle(),
            lessonDto.getContent(),
            lessonDto.getVideoUrl(),
            lessonDto.getOrderIndex() != null ? lessonDto.getOrderIndex() : 0,
            course
        );

        return lessonRepository.save(lesson);
    }

    /**
     * Met à jour une leçon
     * 
     * RÉSOUT: Un enseignant modifie sa leçon
     */
    public Lesson updateLesson(Long id, LessonDto lessonDto) {
        Optional<Lesson> lessonOpt = lessonRepository.findById(id);
        if (!lessonOpt.isPresent()) {
            throw new RuntimeException("Leçon introuvable avec l'ID: " + id);
        }

        Lesson lesson = lessonOpt.get();
        lesson.setTitle(lessonDto.getTitle());
        lesson.setContent(lessonDto.getContent());
        lesson.setVideoUrl(lessonDto.getVideoUrl());
        
        if (lessonDto.getOrderIndex() != null) {
            lesson.setOrderIndex(lessonDto.getOrderIndex());
        }

        return lessonRepository.save(lesson);
    }

    /**
     * Récupère les leçons d'un cours
     * 
     * RÉSOUT: Afficher le contenu d'un cours
     */
    public List<Lesson> getLessonsByCourseId(Long courseId) {
        return lessonRepository.findByCourseId(courseId);
    }

    /**
     * Récupère une leçon par ID
     * 
     * RÉSOUT: Afficher les détails d'une leçon
     */
    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    /**
     * Supprime une leçon
     * 
     * RÉSOUT: Un enseignant supprime une leçon
     */
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    /**
     * Compte le nombre de leçons dans un cours
     * 
     * RÉSOUT: Afficher le nombre de leçons
     */
    public long countLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseId(courseId).size();
    }
}