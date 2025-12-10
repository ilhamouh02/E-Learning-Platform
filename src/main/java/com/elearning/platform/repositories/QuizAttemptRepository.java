package com.elearning.platform.repositories;

import com.elearning.platform.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    Optional<QuizAttempt> findByQuizIdAndStudentId(Long quizId, Long userId);
    List<QuizAttempt> findByStudentId(Long userId);
    List<QuizAttempt> findByQuizId(Long quizId);
}