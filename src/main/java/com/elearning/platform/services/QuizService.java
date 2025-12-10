package com.elearning.platform.services;

import com.elearning.platform.dto.QuizDto;
import com.elearning.platform.dto.QuizSubmitDto;
import com.elearning.platform.model.*;
import com.elearning.platform.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    public Quiz createQuiz(QuizDto quizDto) {
        Optional<Lesson> lessonOpt = lessonRepository.findById(quizDto.getLessonId());
        if (!lessonOpt.isPresent()) {
            throw new RuntimeException("Leçon introuvable avec l'ID: " + quizDto.getLessonId());
        }

        Lesson lesson = lessonOpt.get();

        Quiz quiz = new Quiz(
            quizDto.getTitle(),
            quizDto.getTimeLimit(),
            quizDto.getPassingScore(),
            lesson
        );

        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(Long id, QuizDto quizDto) {
        Optional<Quiz> quizOpt = quizRepository.findById(id);
        if (!quizOpt.isPresent()) {
            throw new RuntimeException("Quiz introuvable avec l'ID: " + id);
        }

        Quiz quiz = quizOpt.get();
        quiz.setTitle(quizDto.getTitle());
        quiz.setTimeLimit(quizDto.getTimeLimit());
        quiz.setPassingScore(quizDto.getPassingScore());

        return quizRepository.save(quiz);
    }

    public List<Quiz> getQuizzesByLessonId(Long lessonId) {
        return quizRepository.findByLessonId(lessonId);
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    public Map<String, Object> submitQuiz(QuizSubmitDto submitDto, User user) {
        Optional<Quiz> quizOpt = quizRepository.findById(submitDto.getQuizId());
        if (!quizOpt.isPresent()) {
            throw new RuntimeException("Quiz introuvable");
        }

        Quiz quiz = quizOpt.get();
        List<Question> questions = questionRepository.findByQuizId(quiz.getId());

        // Calculer le score
        int totalScore = 0;
        int earnedScore = 0;

        for (Question q : questions) {
            totalScore += (q.getPoints() != null ? q.getPoints() : 1);
            String userAnswer = submitDto.getAnswers().get(q.getId());
            if (userAnswer != null && userAnswer.equals(q.getCorrectAnswer())) {
                earnedScore += (q.getPoints() != null ? q.getPoints() : 1);
            }
        }

        int percentage = totalScore > 0 ? (earnedScore * 100) / totalScore : 0;

        // Sauvegarder la tentative
        QuizAttempt attempt = new QuizAttempt(quiz, user, percentage);
        quizAttemptRepository.save(attempt);

        // Retourner les résultats
        Map<String, Object> result = new HashMap<>();
        result.put("score", percentage);
        result.put("passed", percentage >= quiz.getPassingScore());
        result.put("totalQuestions", questions.size());
        result.put("correctAnswers", earnedScore);

        return result;
    }

    public Map<String, Object> getQuizResults(Long quizId) {
        List<Question> questions = questionRepository.findByQuizId(quizId);

        int totalPoints = questions.stream()
            .mapToInt(q -> q.getPoints() != null ? q.getPoints() : 1)
            .sum();

        Map<String, Object> result = new HashMap<>();
        result.put("totalQuestions", questions.size());
        result.put("totalPoints", totalPoints);
        result.put("questions", questions);

        return result;
    }
}