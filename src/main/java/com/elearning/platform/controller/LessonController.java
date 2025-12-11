package com.elearning.platform.controller;

import com.elearning.platform.dto.LessonDto;
import com.elearning.platform.model.Lesson;
import com.elearning.platform.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lessons")
@CrossOrigin(origins = "*")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Lesson>> getLessonsByCourse(@PathVariable Long courseId) {
        List<Lesson> lessons = lessonService.getLessonsByCourseId(courseId);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long lessonId) {
        Optional<Lesson> lesson = lessonService.getLessonById(lessonId);
        if (lesson.isPresent()) {
            return ResponseEntity.ok(lesson.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Lesson> createLesson(@Valid @RequestBody LessonDto lessonDto) {
        Lesson lesson = lessonService.createLesson(lessonDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(lesson);
    }

    @PutMapping("/{lessonId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonDto lessonDto) {
        Lesson updatedLesson = lessonService.updateLesson(lessonId, lessonDto);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/{lessonId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }
}