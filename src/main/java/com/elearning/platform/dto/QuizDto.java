package com.elearning.platform.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;

public class QuizDto {
    private Long quizId;

    @NotBlank(message = "Le titre du quiz est obligatoire")
    private String title;

    @Min(value = 1, message = "La durée du quiz doit être >= 1 minute")
    private Integer timeLimit;

    @Min(value = 0, message = "Le score de passage doit être >= 0")
    private Integer passingScore;

    private Long lessonId;
    
    // Constructeurs
    public QuizDto() {}
    
    public QuizDto(Long quizId, String title, Integer timeLimit, Integer passingScore, Long lessonId) {
        this.quizId = quizId;
        this.title = title;
        this.timeLimit = timeLimit;
        this.passingScore = passingScore;
        this.lessonId = lessonId;
    }
    
    // Getters et Setters
    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public Integer getTimeLimit() { return timeLimit; }
    public void setTimeLimit(Integer timeLimit) { this.timeLimit = timeLimit; }
    
    public Integer getPassingScore() { return passingScore; }
    public void setPassingScore(Integer passingScore) { this.passingScore = passingScore; }
    
    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }
}