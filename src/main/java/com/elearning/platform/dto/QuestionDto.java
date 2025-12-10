package com.elearning.platform.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class QuestionDto {
    
    private Long id;
    
    @NotBlank(message = "Le texte de la question est obligatoire")
    private String questionText;
    
    private String options; // Format JSON
    
    @NotBlank(message = "La réponse correcte est obligatoire")
    private String correctAnswer;
    
    @Min(value = 1, message = "Les points doivent être >= 1")
    private Integer points;
    
    private Long quizId;
    
    // Constructeurs
    public QuestionDto() {}
    
    public QuestionDto(Long id, String questionText, String options, String correctAnswer, Integer points, Long quizId) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.points = points;
        this.quizId = quizId;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    
    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }
    
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    
    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }
}