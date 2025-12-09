package com.elearning.platform.services;

import com.elearning.platform.dto.QuestionDto;
import com.elearning.platform.model.Question;
import com.elearning.platform.model.Quiz;
import com.elearning.platform.repositories.QuestionRepository;
import com.elearning.platform.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<Question> getQuestionsByQuiz(Long quizId) {
        return questionRepository.findByQuiz_QuizId(quizId);
    }

    public Optional<Question> getQuestionById(Long questionId) {
        return questionRepository.findById(questionId);
    }

    public Question createQuestion(QuestionDto questionDto) {
        Quiz quiz = quizRepository.findById(questionDto.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz non trouvé: " + questionDto.getQuizId()));

        Question question = new Question();
        question.setQuestionText(questionDto.getQuestionText());
        question.setOptions(questionDto.getOptions());
        question.setCorrectAnswer(questionDto.getCorrectAnswer());
        question.setPoints(questionDto.getPoints() != null ? questionDto.getPoints() : 1);
        question.setQuiz(quiz);

        return questionRepository.save(question);
    }

    public Question updateQuestion(Long questionId, QuestionDto questionDto) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question non trouvée: " + questionId));

        question.setQuestionText(questionDto.getQuestionText());
        question.setOptions(questionDto.getOptions());
        question.setCorrectAnswer(questionDto.getCorrectAnswer());
        question.setPoints(questionDto.getPoints());

        return questionRepository.save(question);
    }

    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }
}
