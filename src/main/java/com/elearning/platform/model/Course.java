package com.elearning.platform.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entité Course - Cours de la plateforme
 * Chemin: src/main/java/com/elearning/platform/model/Course.java
 * 
 * Chaque cours a un enseignant et peut avoir plusieurs leçons et étudiants inscrits
 */
@Entity
@Table(name = "courses", indexes = {
    @Index(name = "idx_teacher_id", columnList = "teacher_id"),
    @Index(name = "idx_category", columnList = "category")
})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String category;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relation avec l'enseignant (professeur)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = true)
    private User teacher;

    // Relation avec les leçons
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    // Relation avec les inscriptions
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;

    // Constructeurs
    public Course() {
        this.createdAt = LocalDateTime.now();
    }

    public Course(String title, String description, String category, User teacher) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.teacher = teacher;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getTeacher() { return teacher; }
    public void setTeacher(User teacher) { this.teacher = teacher; }

    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }

    public List<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }
}