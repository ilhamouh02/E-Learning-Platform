package com.elearning.platform.model;

import javax.persistence.*;

/**
 * Entité Question - Question d'un quiz
 * Chemin: src/main/java/com/elearning/platform/model/Question.java
 * 
 * Chaque question appartient à un quiz
 * Options est stocké en JSON
 */
@Entity
@Table(name = "questions", indexes = {
    @Index(name = "idx_quiz_id", columnList = "quiz_id")
})
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Column(columnDefinition = "JSON")
    private String options; // Format JSON: ["option1", "option2", ...]

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @Column
    private Integer points = 1;

    // Relation avec le quiz (parent)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    // Constructeurs
    public Question() {
        this.points = 1;
    }

    public Question(String questionText, String options, String correctAnswer, Integer points, Quiz quiz) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.points = points != null ? points : 1;
        this.quiz = quiz;
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

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
}