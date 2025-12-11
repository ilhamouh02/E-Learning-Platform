package com.elearning.platform.repositories;

import com.elearning.platform.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacherId(Long teacherId);
    List<Course> findByCategory(String category);
    List<Course> findByCategoryAndTeacherId(String category, Long teacherId);
}