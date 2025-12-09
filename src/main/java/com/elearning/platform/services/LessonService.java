package com.elearning.platform.services;

import com.elearning.platform.dto.LessonDto;
import com.elearning.platform.model.Course;
import com.elearning.platform.model.Lesson;
import com.elearning.platform.repositories.LessonRepository;
import com.elearning.platform.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourse_CourseIdOrderByOrderIndexAsc(courseId);
    }

    public Optional<Lesson> getLessonById(Long lessonId) {
        return lessonRepository.findById(lessonId);
    }

    public Lesson createLesson(LessonDto lessonDto) {
        Course course = courseRepository.findById(lessonDto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Cours non trouvé: " + lessonDto.getCourseId()));

        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDto.getTitle());
        lesson.setContent(lessonDto.getContent());
        lesson.setVideoUrl(lessonDto.getVideoUrl());
        lesson.setOrderIndex(lessonDto.getOrderIndex() != null ? lessonDto.getOrderIndex() : 0);
        lesson.setCourse(course);

        return lessonRepository.save(lesson);
    }

    public Lesson updateLesson(Long lessonId, LessonDto lessonDto) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Leçon non trouvée: " + lessonId));

        lesson.setTitle(lessonDto.getTitle());
        lesson.setContent(lessonDto.getContent());
        lesson.setVideoUrl(lessonDto.getVideoUrl());
        lesson.setOrderIndex(lessonDto.getOrderIndex());

        return lessonRepository.save(lesson);
    }

    public void deleteLesson(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }
}
