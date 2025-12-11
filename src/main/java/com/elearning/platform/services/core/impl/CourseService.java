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

@Service
public class CourseService implements GenericService<CourseDto, Course> {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

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

    @Override
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public Course update(CourseDto courseDto, Long id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.setTitle(courseDto.getTitle());
            course.setDescription(courseDto.getDescription());
            course.setCategory(courseDto.getCategory());

            // Mettre à jour l'enseignant si nécessaire
            if (courseDto.getTeacherId() != null) {
                Optional<User> teacherOpt = userRepository.findById(courseDto.getTeacherId());
                teacherOpt.ifPresent(course::setTeacher);
            }

            return courseRepository.save(course);
        }
        return null;
    }

    public List<Course> findByTeacherId(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

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
}