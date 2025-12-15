package com.elearning.platform.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * QuizDto - Data Transfer Object pour Quiz
 * Chemin: src/main/java/com/elearning/platform/dto/QuizDto.java
 * 
 * Utilisé pour créer/modifier les quizzes
 */
public class QuizDto {
    
    private Long id;

    @NotBlank(message = "Le titre du quiz est obligatoire")
    private String title;

    @Min(value = 1, message = "La durée du quiz doit être >= 1 minute")
    private Integer timeLimit; // en minutes

    @Min(value = 0, message = "Le score de passage doit être >= 0")
    private Integer passingScore; // en pourcentage

    private Long lessonId;
    
    // Constructeurs
    public QuizDto() {}
    
    public QuizDto(Long id, String title, Integer timeLimit, Integer passingScore, Long lessonId) {
        this.id = id;
        this.title = title;
        this.timeLimit = timeLimit;
        this.passingScore = passingScore;
        this.lessonId = lessonId;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public Integer getTimeLimit() { return timeLimit; }
    public void setTimeLimit(Integer timeLimit) { this.timeLimit = timeLimit; }
    
    public Integer getPassingScore() { return passingScore; }
    public void setPassingScore(Integer passingScore) { this.passingScore = passingScore; }
    
    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }
}