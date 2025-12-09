package com.elearning.platform.repositories;

import com.elearning.platform.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent_UserId(Long userId);
    List<Enrollment> findByCourse_CourseId(Long courseId);
    Optional<Enrollment> findByStudent_UserIdAndCourse_CourseId(Long userId, Long courseId);
}
