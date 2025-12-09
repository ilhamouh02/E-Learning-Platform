package com.elearning.platform.services.core.impl;

import com.elearning.platform.model.User;
import com.elearning.platform.model.UserRepository;
import com.elearning.platform.model.Course;
import com.elearning.platform.model.Enrollment;
import com.elearning.platform.repositories.CourseRepository;
import com.elearning.platform.repositories.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EnrollmentService {

    private EnrollmentRepository enrollmentRepository;
    private CourseRepository courseRepository;
    private UserRepository userRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public void createEnrollment(Long courseId, String username) throws Exception {
        Course course = courseRepository.findById(courseId).get();
        User user = userRepository.findByUsername(username);

        if (user != null) {
            // Note: user.getId() dépend de la structure de com.elearning.platform.auth.User
            // Ajuster selon votre implémentation réelle
            Optional<com.elearning.platform.model.Enrollment> existing = enrollmentRepository
                    .findByStudent_UserIdAndCourse_CourseId(user.getUserId(), course.getCourseId());
            if (existing.isPresent()) {
                throw new Exception("You already enrolled in this course");
            }
        }
        LocalDate date = LocalDate.now();
        // Note: Créer un objet Enrollment avec le constructeur mis à jour
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(date);
        enrollment.setProgress(0);
        enrollment.setCompleted(false);
        // link student and course
        if (user != null) {
            enrollment.setStudent(user);
        }
        enrollment.setCourse(course);
        enrollmentRepository.save(enrollment);
    }
}
