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

@Service
public class EnrollmentService implements GenericService<EnrollmentDto, Enrollment> {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

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
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .build();
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public List<Enrollment> getAll() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Enrollment findById(Long id) {
        // Note: Enrollment a une clé composite, cette méthode n'est pas vraiment utilisée
        return null;
    }

    public List<Enrollment> findByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> findByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    @Override
    public void deleteById(Long id) {
        // Non applicable pour Enrollment (clé composite)
    }

    public void deleteByStudentIdAndCourseId(Long studentId, Long courseId) {
        enrollmentRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public Enrollment update(EnrollmentDto enrollmentDto, Long id) {
        // Trouver l'inscription existante
        Optional<User> userOpt = userRepository.findById(enrollmentDto.getStudentId());
        Optional<Course> courseOpt = courseRepository.findById(enrollmentDto.getCourseId());

        if (!userOpt.isPresent() || !courseOpt.isPresent()) {
            return null;
        }

        User student = userOpt.get();
        Course course = courseOpt.get();

        // Créer/mettre à jour l'inscription
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .progress(enrollmentDto.getProgress())
                .completed(enrollmentDto.getCompleted())
                .build();

        return enrollmentRepository.save(enrollment);
    }
}