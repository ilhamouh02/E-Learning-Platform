package com.elearning.platform.controller;

import com.elearning.platform.dto.CourseDto;
import com.elearning.platform.model.Course;
import com.elearning.platform.services.core.impl.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long teacherId) {
        List<Course> courses = courseService.search(category, teacherId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        Course course = courseService.findById(courseId);
        if (course != null) {
            return ResponseEntity.ok(course);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseDto courseDto) {
        Course course = courseService.save(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseDto courseDto) {
        Course updatedCourse = courseService.update(courseDto, courseId);
        if (updatedCourse != null) {
            return ResponseEntity.ok(updatedCourse);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteById(courseId);
        return ResponseEntity.noContent().build();
    }
}
