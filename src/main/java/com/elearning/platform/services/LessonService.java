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

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

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

    public Lesson updateLesson(Long id, LessonDto lessonDto) {
        Optional<Lesson> lessonOpt = lessonRepository.findById(id);
        if (!lessonOpt.isPresent()) {
            throw new RuntimeException("Leçon introuvable avec l'ID: " + id);
        }

        Lesson lesson = lessonOpt.get();
        lesson.setTitle(lessonDto.getTitle());
        lesson.setContent(lessonDto.getContent());
        lesson.setVideoUrl(lessonDto.getVideoUrl());

        return lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonsByCourseId(Long courseId) {
        return lessonRepository.findByCourseId(courseId);
    }

    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}