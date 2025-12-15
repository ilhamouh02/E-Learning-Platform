package com.elearning.platform.model;

import javax.persistence.*;
import java.util.List;

/**
 * Entité Quiz - Évaluation d'une leçon
 * Chemin: src/main/java/com/elearning/platform/model/Quiz.java
 * 
 * Chaque quiz appartient à une leçon et a plusieurs questions
 * Propriétés: timeLimit (minutes), passingScore (%)
 */
@Entity
@Table(name = "quizzes", indexes = {
    @Index(name = "idx_lesson_id", columnList = "lesson_id")
})
public class Quiz {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "time_limit")
    private Integer timeLimit; // en minutes

    @Column(name = "passing_score")
    private Integer passingScore = 70; // en pourcentage

    // Relation avec la leçon (parent)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    // Relation avec les questions
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    // Relation avec les tentatives
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuizAttempt> attempts;

    // Constructeurs
    public Quiz() {
        this.passingScore = 70;
    }

    public Quiz(String title, Integer timeLimit, Integer passingScore, Lesson lesson) {
        this.title = title;
        this.timeLimit = timeLimit;
        this.passingScore = passingScore != null ? passingScore : 70;
        this.lesson = lesson;
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

    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public List<QuizAttempt> getAttempts() { return attempts; }
    public void setAttempts(List<QuizAttempt> attempts) { this.attempts = attempts; }
}