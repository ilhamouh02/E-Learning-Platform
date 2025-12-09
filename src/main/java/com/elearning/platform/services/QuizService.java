package com.elearning.platform.services;

import com.elearning.platform.dto.QuizDto;
import com.elearning.platform.dto.QuizSubmitDto;
import com.elearning.platform.model.*;
import com.elearning.platform.repositories.QuizRepository;
import com.elearning.platform.repositories.QuestionRepository;
import com.elearning.platform.repositories.QuizAttemptRepository;
import com.elearning.platform.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private LessonRepository lessonRepository;

    public List<Quiz> findQuizzesByLesson(Long lessonId) {
        return quizRepository.findByLesson_LessonId(lessonId);
    }

    public Optional<Quiz> findQuizById(Long quizId) {
        return quizRepository.findById(quizId);
    }

    public Quiz createQuiz(QuizDto quizDto) {
        Optional<Lesson> lesson = lessonRepository.findById(quizDto.getLessonId());
        if (!lesson.isPresent()) {
            throw new RuntimeException("Leçon non trouvée: " + quizDto.getLessonId());
        }

        Quiz quiz = new Quiz();
        quiz.setTitle(quizDto.getTitle());
        quiz.setTimeLimit(quizDto.getTimeLimit());
        quiz.setPassingScore(quizDto.getPassingScore());
        quiz.setLesson(lesson.get());

        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(Long quizId, QuizDto quizDto) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz non trouvé: " + quizId));

        quiz.setTitle(quizDto.getTitle());
        quiz.setTimeLimit(quizDto.getTimeLimit());
        quiz.setPassingScore(quizDto.getPassingScore());

        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long quizId) {
        quizRepository.deleteById(quizId);
    }

    public List<Question> getQuestionsForQuiz(Long quizId) {
        return questionRepository.findByQuiz_QuizId(quizId);
    }

    public QuizAttempt submitQuiz(Long quizId, User user, QuizSubmitDto submitDto) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz non trouvé: " + quizId));

        // Calculer le score
        int score = calculateScore(quizId, submitDto.getAnswers());

        // Vérifier s'il existe déjà une tentative (unique par quiz/user)
        Optional<QuizAttempt> existingAttempt = quizAttemptRepository
                .findByQuiz_QuizIdAndUser_UserId(quizId, user.getUserId());

        QuizAttempt attempt;
        if (existingAttempt.isPresent()) {
            attempt = existingAttempt.get();
            attempt.setScore(score);
            attempt.setCompletedAt(LocalDateTime.now());
        } else {
            attempt = new QuizAttempt();
            attempt.setQuiz(quiz);
            attempt.setUser(user);
            attempt.setScore(score);
            attempt.setCompletedAt(LocalDateTime.now());
        }

        return quizAttemptRepository.save(attempt);
    }

    private int calculateScore(Long quizId, java.util.Map<Long, String> answers) {
        List<Question> questions = questionRepository.findByQuiz_QuizId(quizId);
        if (questions.isEmpty()) {
            return 0;
        }

        int totalPoints = questions.stream().mapToInt(q -> q.getPoints() != null ? q.getPoints() : 1).sum();
        if (totalPoints == 0) {
            return 0;
        }

        int earnedPoints = 0;
        for (Question question : questions) {
            String userAnswer = answers.getOrDefault(question.getQuestionId(), "");
            if (userAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
                earnedPoints += (question.getPoints() != null ? question.getPoints() : 1);
            }
        }

        return (int) ((earnedPoints / (double) totalPoints) * 100);
    }

    public Optional<QuizAttempt> getAttemptForQuiz(Long quizId, Long userId) {
        return quizAttemptRepository.findByQuiz_QuizIdAndUser_UserId(quizId, userId);
    }

    public List<QuizAttempt> getAttemptsByUser(Long userId) {
        return quizAttemptRepository.findByUser_UserId(userId);
    }
}
