package com.elearning.platform.repositories;

import com.elearning.platform.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourse_CourseIdOrderByOrderIndexAsc(Long courseId);
    List<Lesson> findByTitle(String title);
}
