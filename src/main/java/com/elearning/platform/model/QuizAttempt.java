package com.elearning.platform.model;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité QuizAttempt - Tentative de quiz par un étudiant
 * Chemin: src/main/java/com/elearning/platform/model/QuizAttempt.java
 * 
 * Enregistre les résultats des quiz
 * Unique: (quiz_id, student_id)
 */
@Entity
@Table(name = "quiz_attempts", indexes = {
    @Index(name = "idx_quiz_id", columnList = "quiz_id"),
    @Index(name = "idx_student_id", columnList = "student_id"),
    @Index(name = "unique_attempt", columnList = "quiz_id,student_id", unique = true)
})
public class QuizAttempt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation avec le quiz
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    // Relation avec l'étudiant
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column
    private Integer score; // En pourcentage (0-100)

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;

    // Constructeurs
    public QuizAttempt() {}

    public QuizAttempt(Quiz quiz, User student, Integer score) {
        this.quiz = quiz;
        this.student = student;
        this.score = score;
        this.completedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (completedAt == null) {
            completedAt = LocalDateTime.now();
        }
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }

    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    // Utilitaire
    public boolean isPassed() {
        return quiz != null && score >= quiz.getPassingScore();
    }
}