package com.elearning.platform.services;

import com.elearning.platform.model.Enrollment;
import com.elearning.platform.model.User;
import com.elearning.platform.model.Course;
import com.elearning.platform.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<Enrollment> getEnrollmentsByStudent(User student) {
        return enrollmentRepository.findByStudent_UserId(student.getUserId());
    }

    public Optional<Enrollment> getEnrollment(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId);
    }

    public Enrollment enrollStudent(User student, Course course) {
        // Vérifier que l'étudiant n'est pas déjà inscrit
        Optional<Enrollment> existing = enrollmentRepository
                .findByStudent_UserIdAndCourse_CourseId(student.getUserId(), course.getCourseId());
        
        if (existing.isPresent()) {
            throw new RuntimeException("L'étudiant est déjà inscrit à ce cours");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setProgress(0);
        enrollment.setCompleted(false);

        return enrollmentRepository.save(enrollment);
    }

    public Enrollment updateProgress(Long enrollmentId, Integer progress) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée: " + enrollmentId));

        if (progress < 0 || progress > 100) {
            throw new RuntimeException("La progression doit être entre 0 et 100");
        }

        enrollment.setProgress(progress);
        if (progress == 100) {
            enrollment.setCompleted(true);
        }

        return enrollmentRepository.save(enrollment);
    }

    public void unenrollStudent(Long enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourse_CourseId(courseId);
    }
}
