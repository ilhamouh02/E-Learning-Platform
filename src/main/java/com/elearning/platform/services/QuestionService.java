package com.elearning.platform.services;

import com.elearning.platform.dto.QuestionDto;
import com.elearning.platform.model.Question;
import com.elearning.platform.model.Quiz;
import com.elearning.platform.repositories.QuestionRepository;
import com.elearning.platform.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public Question createQuestion(QuestionDto questionDto) {
        // Vérifier que le quiz existe
        Optional<Quiz> quizOpt = quizRepository.findById(questionDto.getQuizId());
        if (!quizOpt.isPresent()) {
            throw new RuntimeException("Quiz introuvable avec l'ID: " + questionDto.getQuizId());
        }

        Quiz quiz = quizOpt.get();

        // Créer la question
        Question question = new Question(
            questionDto.getQuestionText(),
            questionDto.getOptions(),
            questionDto.getCorrectAnswer(),
            questionDto.getPoints() != null ? questionDto.getPoints() : 1,
            quiz
        );

        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, QuestionDto questionDto) {
        Optional<Question> questionOpt = questionRepository.findById(id);
        if (!questionOpt.isPresent()) {
            throw new RuntimeException("Question introuvable avec l'ID: " + id);
        }

        Question question = questionOpt.get();
        question.setQuestionText(questionDto.getQuestionText());
        question.setOptions(questionDto.getOptions());
        question.setCorrectAnswer(questionDto.getCorrectAnswer());
        question.setPoints(questionDto.getPoints());

        return questionRepository.save(question);
    }

    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}