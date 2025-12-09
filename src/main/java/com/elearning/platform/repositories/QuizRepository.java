package com.elearning.platform.repositories;

import com.elearning.platform.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByLesson_LessonId(Long lessonId);
    List<Quiz> findByTitle(String title);
}
