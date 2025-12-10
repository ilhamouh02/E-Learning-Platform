package com.elearning.platform.dto;

import java.util.Map;

public class QuizSubmitDto {
    private Long quizId;
    private Long studentId;
    private Map<Long, String> answers; // questionId -> answer
    
    // Constructeurs
    public QuizSubmitDto() {}
    
    public QuizSubmitDto(Long quizId, Long studentId, Map<Long, String> answers) {
        this.quizId = quizId;
        this.studentId = studentId;
        this.answers = answers;
    }
    
    // Getters et Setters
    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }
    
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    
    public Map<Long, String> getAnswers() { return answers; }
    public void setAnswers(Map<Long, String> answers) { this.answers = answers; }
}