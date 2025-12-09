package com.elearning.platform.repositories;

import com.elearning.platform.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    Optional<QuizAttempt> findByQuiz_QuizIdAndUser_UserId(Long quizId, Long userId);
    List<QuizAttempt> findByUser_UserId(Long userId);
    List<QuizAttempt> findByQuiz_QuizId(Long quizId);
}
